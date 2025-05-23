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

package org.openurp.tool.web.ws

import org.beangle.commons.json.JsonObject
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.annotation.{mapping, param, response}
import org.beangle.webmvc.support.ActionSupport
import org.openurp.tool.book.model.AbstractBook
import org.openurp.tool.book.service.{BookService, IsbnHelper}

class BookWS extends ActionSupport {

  var entityDao: EntityDao = _

  var bookService: BookService = _

  @response
  @mapping("isbn/{isbn}")
  def index(@param("isbn") isbnstr: String): JsonObject = {
    val json = new JsonObject
    json.add("success", false)

    val isbn = IsbnHelper.format(isbnstr)
    val rs = IsbnHelper.validate(isbn)
    if (rs._1) {
      val book = bookService.findLocal(isbn)
      book match {
        case None =>
          bookService.findRemote(isbn) match {
            case None =>
              json.add("msg", "查无此书")
            case Some(nb) =>
              entityDao.saveOrUpdate(nb)
              json.add("success", true)
              json.add("data", toJson(nb))
          }
        case Some(b) =>
          json.add("success", true)
          json.add("data", toJson(b))
      }
    } else {
      json.add("msg", rs._2)
    }
    json
  }

  private def toJson(book: AbstractBook): JsonObject = {
    val d = new JsonObject()
    d.add("isbn", book.isbn)
    d.add("name", book.name)
    d.add("author", book.author)
    d.add("press", book.press.name)
    d.add("publishedOn", book.publishedOn)
    d.add("edition", book.edition)
    book.price foreach { p => d.add("price", p) }
    book.description foreach { p => d.add("description", p) }
    book.category foreach { p => d.add("category", p.name) }
    book.binding foreach { p => d.add("binding", p) }
    book.language foreach { p => d.add("language", p) }
    book.format foreach { p => d.add("format", p) }
    book.pages foreach { p => d.add("pages", p) }
    book.words foreach { p => d.add("words", p) }
    book.pictureUrl foreach { p => d.add("pictureUrl", p) }
    d
  }

}
