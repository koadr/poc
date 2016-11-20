package com.trovimap.domain

import java.net.URL
import java.util.Currency

final case class Location(latitude: Long, longitude: Long)

final case class Price(cents: Int, currency: Currency)

final case class BrokerId(id: String) //extends AnyVal

final case class PropertyId(id: String) //extends AnyVal

object Property {
  type Attributes = Map[String, Any]
}

final case class Property(id: PropertyId,
                          brokerId: BrokerId,
                          location: Option[Location],
                          numberOfBathrooms: Int,
                          numberOfBedrooms: Int,
                          title: Option[String],
                          price: Price,
                          photoUrl: Option[URL]) {
  // TODO Add behaviors that belong on a property. These are all pure operations/behaviors

  def associatePrice(price: Price): Property = this.copy(price = price)
}
