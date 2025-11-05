package org.openurp.tool.web.ws

import org.beangle.commons.json.JsonObject
import org.beangle.webmvc.annotation.{param, response}
import org.beangle.webmvc.support.ActionSupport
import org.openurp.tool.enterprise.service.EnterpriseService

class EnterpriseWS extends ActionSupport {

  var enterpriseService: EnterpriseService = _

  @response
  def search(@param("q") q: String): collection.Seq[JsonObject] = {
    enterpriseService.find(q).map { e =>
      val d = new JsonObject()
      d.add("name", e.name)
      d.add("uniscid", e.uniscid)
      d.add("frName", e.frName)
      d.add("address", e.address)
      d.add("establishOn", e.establishOn)
      d.add("registeredCaptital", e.registeredCaptital)
      d.add("status",e.status)
    }
  }
}
