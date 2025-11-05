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
