package com.trovimap.infrastructure.http.dto

import java.net.URL

import com.trovimap.domain._

final case class PropertyDTO(id: String,
                             brokerId: String,
                             location: Option[Location],
                             numberOfBathrooms: Int,
                             numberOfBedrooms: Int,
                             title: Option[String],
                             price: Price,
                             photoUrl: Option[String]) {
  def property =
    Property(PropertyId(id),
             BrokerId(brokerId),
             location,
             numberOfBathrooms,
             numberOfBedrooms,
             title,
             price,
             photoUrl.map(new URL(_)))
}
