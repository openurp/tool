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

import org.beangle.data.orm.{IdGenerator, MappingModule}

class DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultCache("openurp.tool", "read-write")

    bind[BookCategory].declare { e =>
      e.code is length(50)
      e.name is length(100)
    }.cacheable()

    bind[Press].declare { e =>
      e.code is length(50)
      e.name is length(100)
    }.cacheable()

    bind[AbstractBook].declare { e =>
      e.isbn is length(13)
      e.name is length(200)
      e.author is length(100)
      e.edition is length(20)
      e.translator is length(100)

      e.binding is length(10)
      e.words is length(30)
      e.pages is length(10)
      e.format is length(10)
      e.language is length(60)
      e.pictureUrl is length(200)

      e.description is length(500)
    }

    bind[Book].declare { e =>
      index("", true, e.isbn)
    }

    bind[HisBook].declare { e =>
      index("", true, e.isbn)
    }.generator(IdGenerator.Assigned)

  }
}
