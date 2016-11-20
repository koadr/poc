package com.trovimap.domain

import scala.concurrent.Future

trait PropertyRepository {
  def getPropertyById(id: PropertyId): Future[Option[Property]]
  def createProperty(property: Property): Future[Option[Property]]
  def updateProperty(property: Property): Future[Option[Property]]
}
