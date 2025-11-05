package org.openurp.tool.enterprise.service

import org.openurp.tool.enterprise.model.Enterprise

trait EnterpriseService {
  def find(key: String): collection.Seq[Enterprise]
}
