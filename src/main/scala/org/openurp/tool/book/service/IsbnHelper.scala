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

package org.openurp.tool.book.service

import org.beangle.commons.lang.Strings

object IsbnHelper {

  def validate(isbn: String): (Boolean, String) = {
    if (null == isbn) {
      (false, "ISBN不能为空")
    } else if (isbn.length != 13) {
      (false, "ISBN位数不够，应该为13位")
    } else if (!isbn.startsWith("978")) {
      (false, "ISBN应以978开头")
    } else {
      val chars = isbn.toCharArray
      val isAllNum = chars.forall(x => x >= '0' && x <= '9')
      if (isAllNum) {
        var sum = 0
        var i = 0
        var multiple = 3
        while (i < chars.length - 1) { //最后一位是校验位
          multiple = if multiple == 1 then 3 else 1
          sum += (chars(i) - '0') * multiple
          i += 1
        }
        val checkSum = if sum % 10 == 0 then 0 else 10 - (sum % 10)
        if ((chars(12) - '0') == checkSum) {
          (true, "")
        } else {
          (false, "ISBN格式不正确，校验失败")
        }
      } else {
        (false, "ISBN中只能包含0-9的数字")
      }
    }
  }

  def format(isbn: String): String = {
    if (isbn.length > 13) {
      Strings.replace(isbn, "-", "")
    } else {
      isbn
    }
  }

  /** 查找国内ISBN的出版社编码
   *
   * @see https://www.qiuwenbaike.cn/wiki/中华人民共和国出版社列表
   * @param isbn
   * @return
   */
  def getPublisherId(isbn: String): String = {
    //9787x
    if (isbn.startsWith("9787")) {
      val startChar = isbn.charAt(4)
      val len = startChar match {
        case '0' => 2
        case '1' | '2' | '3' | '4' => 3
        case '5' | '6' | '7' => 4
        case '8' => 5
        case '9' => 6
      }
      isbn.substring(4, len + 4)
    } else {
      isbn.substring(4, 4 + 4) //其他国家默认取4位
    }
  }
}
