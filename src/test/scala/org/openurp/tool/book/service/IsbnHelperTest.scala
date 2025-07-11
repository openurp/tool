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

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class IsbnHelperTest extends AnyFunSpec, Matchers {

  describe("IsbnHelper") {
    it("validate") {
      val isbn1 = "978-7-5442-6527-0"
      val isbn2 = "978-7-303-20194-5"
      val isbn3 = "978-7-115-50055-7"
      val rs = IsbnHelper.validate(IsbnHelper.format(isbn2))
      assert(rs._1)
    }
  }
}
