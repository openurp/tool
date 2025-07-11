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

package org.openurp.tool.translate.service

import org.beangle.commons.cdi.BindModule
import org.beangle.ems.app.EmsApp
import org.openurp.tool.translate.service.impl.BaiduTranslateServiceImpl

class DefaultModule extends BindModule {

  protected override def binding(): Unit = {
    println(EmsApp.properties)
    if (EmsApp.properties.contains("fanyiAppid")) {
      bind(classOf[BaiduTranslateServiceImpl])
        .property("appid", EmsApp.properties("fanyiAppid"))
        .property("securityKey", EmsApp.properties("fanyiSecurityKey"))
    }
  }
}
