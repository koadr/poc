package com.trovimap.infrastructure.inmem

import com.trovimap.domain.{Property, PropertyId, PropertyRepository}

import scala.collection.mutable
import scala.concurrent.Future

class InMemoryPropertyRepository extends PropertyRepository {
  private val properties: mutable.Map[PropertyId, Property] = mutable.Map()

  override def getPropertyById(id: PropertyId): Future[Option[Property]] =
    Future.successful(properties.get(id))

  override def createProperty(property: Property): Future[Option[Property]] = {
    val _ = properties.put(property.id, property)
    Future.successful(Some(property))
  }

  override def updateProperty(property: Property): Future[Option[Property]] = {
    val _ = properties.put(property.id, property)
    Future.successful(Some(property))
  }
}
