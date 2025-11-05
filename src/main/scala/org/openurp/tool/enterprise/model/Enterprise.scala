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

package org.openurp.tool.enterprise.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Named

import java.time.LocalDate

/** 企业信息
 */
class Enterprise extends LongId, Named {

  /** 统一社会信用代码 */
  var uniscid: String = _

  /** 法定代表人 */
  var frName: String = _

  /** 注册日期 */
  var establishOn: LocalDate = _

  /** 注册地 */
  var address: String = _

  /** 注册资本 */
  var registeredCaptital: Float = _

  /** 状态 */
  var status: String = _

  override def toString = s"Enterprise(${name}, $uniscid, $frName, $establishOn, $address, $registeredCaptital, $status)"
}
