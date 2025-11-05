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

package org.openurp.tool.enterprise.service

import org.beangle.commons.cdi.BindModule
import org.beangle.ems.app.EmsApp
import org.openurp.tool.enterprise.service.impl.WdylEnterpriseServiceImpl

class DefaultModule extends BindModule {

  protected override def binding(): Unit = {
    if (EmsApp.properties.contains("wdylAppid")) {
      bind(classOf[WdylEnterpriseServiceImpl])
        .property("appid", EmsApp.properties("wdylAppid"))
        .property("secret", EmsApp.properties("wdylSecret"))
    }
  }
}
