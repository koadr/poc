package com.trovimap.api

import com.trovimap.api.data.PropertyData
import com.trovimap.domain.Property.Attributes
import com.trovimap.domain.{Property, PropertyId, PropertyRepository}

import scala.concurrent.Future

@SuppressWarnings(Array("org.wartremover.warts.Any"))
class PropertyApi(propertyRepository: PropertyRepository,
                  queryService: PropertyQueryService) {
  def getProperty(id: PropertyId): Future[Option[Property]] =
    propertyRepository.getPropertyById(id)
  def createProperty(property: Property): Future[Option[Property]] =
    propertyRepository.createProperty(property)
  def updateProperty(property: Property): Future[Option[Property]] =
    propertyRepository.updateProperty(property)

  def properties = new PropertyBuilder(None, None, None, Map())

  class PropertyBuilder(
      private val numberOfBedrooms: Option[Int],
      private val numberOfBathrooms: Option[Int],
      private val id: Option[String],
      private val attributes: Attributes
  ) {
    def search(): Future[Seq[PropertyData]] =
      queryService.searchByAttributes(attributes)
    def withBedrooms(numberOfBedrooms: Int) =
      new PropertyBuilder(
        Some(numberOfBedrooms),
        this.numberOfBathrooms,
        this.id,
        attributes.updated("numberOfBedrooms", numberOfBedrooms))
    def withBathrooms(numberOfBathrooms: Int) =
      new PropertyBuilder(
        this.numberOfBedrooms,
        Some(numberOfBathrooms),
        this.id,
        attributes.updated("numberOfBathrooms", numberOfBathrooms))
    def withId(id: String) =
      new PropertyBuilder(
        this.numberOfBedrooms,
        this.numberOfBathrooms,
        Some(id),
        attributes.updated("numberOfBathrooms", numberOfBathrooms))
  }

}
