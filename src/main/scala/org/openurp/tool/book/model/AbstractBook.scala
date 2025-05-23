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
import org.beangle.data.model.pojo.{Named, Updated}

import java.time.YearMonth

abstract class AbstractBook extends LongId, Named, Updated {
  /** ISBN */
  var isbn: String = _
  /** 编著者 */
  var author: String = _
  /** 译作者 */
  var translator: Option[String] = None
  /** 出版社 */
  var press: Press = _
  /** 版次 */
  var edition: String = _
  /** 价格 (单位为分） */
  var price: Option[Int] = None
  /** 简介 */
  var description: Option[String] = None
  /** 出版日期 */
  var publishedOn: YearMonth = _
  /** 图书分类 */
  var category: Option[BookCategory] = None

  /** 装订方式 */
  var binding: Option[String] = None
  /** 语种 */
  var language: Option[String] = None
  /** 开本 */
  var format: Option[String] = None

  /** 页数 */
  var pages: Option[String] = None
  /** 字数 */
  var words: Option[String] = None
  /** 封面图片地址 */
  var pictureUrl: Option[String] = None
}
