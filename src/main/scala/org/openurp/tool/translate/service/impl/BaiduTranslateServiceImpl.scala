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

package org.openurp.tool.translate.service.impl

import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.json.{Json, JsonObject}
import org.beangle.commons.net.http.HttpUtils
import org.beangle.web.servlet.url.UrlBuilder
import org.openurp.tool.translate.service.TranslateService

import scala.collection.mutable

/**
 * Translate text from baidu
 */
class BaiduTranslateServiceImpl extends TranslateService {
  private val TRANS_API_HOST = "https://fanyi-api.baidu.com/api/trans/vip/translate"

  var appid: String = _
  var securityKey: String = _

  override def translate(text: String, from: String, to: String): String = {
    val queryString = UrlBuilder.encodeParams(buildParams(text, from, to))
    val url = s"${TRANS_API_HOST}?${queryString}"
    val rs = HttpUtils.getText(url).getText
    val j = Json.parse(rs).asInstanceOf[JsonObject]
    if (j.contains("error_msg")) {
      j.getString("error_msg")
    } else {
      j.query("trans_result.[0].dst").get.asInstanceOf[String]
    }
  }

  private def buildParams(query: String, from: String, to: String): collection.Map[String, String] = {
    val params = new mutable.HashMap[String, String]
    params.put("q", query)
    params.put("from", from)
    params.put("to", to)
    params.put("appid", appid)
    // 随机数
    val salt = String.valueOf(System.currentTimeMillis)
    params.put("salt", salt)
    // 签名
    val src = appid + query + salt + securityKey // 加密前的原文

    params.put("sign", Digests.md5Hex(src))
    params
  }

}
