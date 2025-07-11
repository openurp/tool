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

package org.openurp.tool.book.service.impl

import org.beangle.commons.json.{Json, JsonObject}
import org.beangle.commons.lang.Strings
import org.beangle.commons.logging.Logging
import org.beangle.commons.net.http.HttpUtils
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.openurp.tool.book.model.*
import org.openurp.tool.book.service.{BookService, IsbnHelper}

import java.time.{Instant, LocalDate, YearMonth}

class BookServiceImpl extends BookService, Logging {

  var entityDao: EntityDao = _
  var appKey: String = _
  var expiredDays = 5 * 365 + 31 //5 years+plus 1 month

  override def findLocal(isbn: String): Option[AbstractBook] = {
    val books = entityDao.findBy(classOf[Book], "isbn", isbn)
    if (books.isEmpty) {
      val hisbooks = entityDao.findBy(classOf[HisBook], "isbn", isbn)
      hisbooks.headOption
    } else {
      books.headOption
    }
  }

  override def findRemote(isbn: String): Option[AbstractBook] = {
    val url = s"https://data.isbn.work/openApi/getInfoByIsbn?isbn=${isbn}&appKey=${appKey}"
    val res = HttpUtils.getText(url)
    if (logger.isDebugEnabled) {
      logger.debug(s"fetch ${url} get ${res.getText}")
    }
    val r = Json.parse(res.getText).asInstanceOf[JsonObject]
    if (r.getBoolean("success")) {
      val j = r.get("data").map(_.asInstanceOf[JsonObject])
      j.map { jd =>
        var pressDate = jd.getString("pressDate")
        if (Strings.count(pressDate, '-') == 2) {
          pressDate = Strings.substringBeforeLast(pressDate, "-")
        } else if (pressDate.length < 6) {
          pressDate = pressDate.substring(0, 4) + "-01"
        }
        val publishedIn = YearMonth.parse(pressDate)
        val expired = publishedIn.atDay(1).plusDays(expiredDays).isBefore(LocalDate.now)
        val nb = new Book
        nb.updatedAt = Instant.now
        nb.isbn = isbn
        nb.name = jd.getString("bookName")
        nb.author = jd.getString("author")
        nb.publishedIn = publishedIn
        nb.edition = jd.getString("edition")
        if (Strings.isBlank(nb.edition)) {
          nb.edition = "--"
        }
        nb.description = Option(jd.getString("bookDesc", null))
        nb.price = Some(jd.getInt("price"))
        val press = jd.getString("press")
        nb.press = getOrCreatePress(press, Some(IsbnHelper.getPublisherId(isbn)))

        val clcCode = jd.getString("clcCode")
        val clcName = jd.getString("clcName")
        if (Strings.isNotBlank(clcCode) && Strings.isNotBlank(clcName)) {
          nb.category = Some(getOrCreateCategory(clcName, clcCode))
        }
        nb.binding = getString(jd, "binding")
        nb.language = getString(jd, "language")
        nb.format = getString(jd, "format")
        nb.pages = getString(jd, "pages")
        nb.words = getString(jd, "words")
        val pictures = jd.getString("pictures", null)
        if (Strings.isNotBlank(pictures)) {
          val pics = Json.parseArray(pictures)
          if (pics.nonEmpty) {
            nb.pictureUrl = Some(pics.head.asInstanceOf[String])
          }
        }
        entityDao.saveOrUpdate(nb)
        if (expired) {
          val nhb = new HisBook(nb)
          entityDao.saveOrUpdate(nhb)
          entityDao.remove(nb)
          nhb
        } else {
          nb
        }
      }
    } else {
      None
    }
  }

  private def getString(o: JsonObject, p: String): Option[String] = {
    val v = o.getString(p)
    if Strings.isBlank(v) then None else Some(v)
  }

  private def getOrCreatePress(pressName: String, publisherId: Option[String]): Press = {
    val name = pressName.trim()
    val q = OqlBuilder.from(classOf[Press], "p")
    q.where("p.name=:name", name).cacheable()
    val rs = entityDao.search(q)
    rs.headOption match {
      case None =>
        publisherId match {
          case None => null
          case Some(pid) =>
            val p = new Press(pid, name)
            entityDao.saveOrUpdate(p)
            p
        }
      case Some(p) => p
    }
  }

  private def getOrCreateCategory(name: String, code: String): BookCategory = {
    val q = OqlBuilder.from(classOf[BookCategory], "c")
    q.where("c.name=:name", name).cacheable()
    val rs = entityDao.search(q)
    rs.headOption match {
      case None =>
        val p = new BookCategory(code, name)
        entityDao.saveOrUpdate(p)
        p
      case Some(p) => p
    }
  }
}
