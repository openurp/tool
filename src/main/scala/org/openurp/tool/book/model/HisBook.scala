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

package org.openurp.tool.book.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Named

import java.time.YearMonth

/** 图书
 */
class HisBook extends AbstractBook {
  def this(b: Book) = {
    this()
    this.id = b.id
    this.isbn = b.isbn
    this.name = b.name
    this.author = b.author
    this.translator = b.translator
    this.press = b.press
    this.edition = b.edition
    this.price = b.price
    this.description = b.description
    this.publishedIn = b.publishedIn
    this.category = b.category
    this.binding = b.binding
    this.language = b.language
    this.format = b.format
    this.pages = b.pages
    this.words = b.words
    this.pictureUrl = b.pictureUrl
    this.updatedAt = b.updatedAt
  }
}
