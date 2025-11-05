/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.tool.enterprise.service.impl

import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.json.{Json, JsonArray, JsonObject}
import org.beangle.commons.logging.Logging
import org.openurp.tool.enterprise.model.Enterprise
import org.openurp.tool.enterprise.service.EnterpriseService

import java.net.URI
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter
import java.time.{Duration, LocalDateTime}

/** 使用五度易链的网络接口
 *
 * @see https://api.wdsk.net/index
 */
class WdylEnterpriseServiceImpl extends EnterpriseService, Logging {

  private val client = HttpClient.newBuilder.connectTimeout(Duration.ofSeconds(10)) // 连接超时
    .followRedirects(HttpClient.Redirect.NORMAL).build()

  private val timestampPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  var url: String = "https://gateway.qyxqk.com/wdyl/openapi/fuzzy_query/"
  var appid: String = _
  var secret: String = _

  override def find(key: String): collection.Seq[Enterprise] = {
    val requestBody = s"""{"key":"${key}"}"""
    // 计算 SIGN 签名
    val timestamp = generateTimestamp()
    val sign = calculateSign(appid, timestamp, secret, key)
    // 构建 HTTP 请求
    val request = HttpRequest.newBuilder.uri(URI.create(url)).timeout(Duration.ofSeconds(15)) // 响应超时
      .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8)) // JSON 体
      .header("Content-Type", "application/json; charset=UTF-8") // 必须设置 JSON 格式
      .header("Accept", "application/json") // 单独添加一个请求头
      .header("APPID", appid)
      .header("TIMESTAMP", timestamp)
      .header("SIGN", sign)
      .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)) // 响应体按 UTF-8 解析为字符串
    val statusCode = response.statusCode
    val responseBody = response.body

    val j = Json.parseObject(responseBody)
    if (j.get("msg").contains("操作成功") && j.getInt("code") == 200) {
      val data = j.query("data.data").get.asInstanceOf[JsonArray]
      val ents = data.map { d =>
        val data = d.asInstanceOf[JsonObject]
        val e = new Enterprise()
        e.name = data.getString("ENTNAME")
        e.uniscid = data.getString("UNISCID")
        e.frName = data.getString("FRNAME", "--")
        e.address = data.getString("DOM", "--")
        e.establishOn = data.getDate("ESDATE")
        e.registeredCaptital = data.getDouble("regcap", 0d).toFloat
        e.status = data.getString("ENTSTATUS", "未知")
        e
      }
      ents.filter(_.uniscid.nonEmpty)
    } else {
      System.out.printf("GET 响应状态码：%d，响应体：%s%n", statusCode, responseBody)
      if (statusCode < 200 || statusCode >= 300) {
        logger.error("Response: " + responseBody)
        throw new RuntimeException("GET 请求失败：状态码=" + statusCode)
      }
      Seq.empty
    }
  }

  private def generateTimestamp() = {
    LocalDateTime.now().format(timestampPattern)
  }

  private def calculateSign(appId: String, timestamp: String, secret: String, key: String) = {
    val signString = appId + timestamp + secret + key
    Digests.md5Hex(signString)
  }
}
