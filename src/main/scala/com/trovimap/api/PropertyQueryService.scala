package com.trovimap.api

import com.trovimap.api.data.PropertyData
import com.trovimap.domain.Property.Attributes

import scala.concurrent.Future

trait PropertyQueryService {
  def searchByAttributes(attributes: Attributes): Future[Seq[PropertyData]]
}
