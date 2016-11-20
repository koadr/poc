package com.trovimap.api.data

import com.trovimap.domain.{Location, Price}

final case class PropertyData(id: String,
                              brokerId: String,
                              location: Option[Location],
                              numberOfBathrooms: Int,
                              numberOfBedrooms: Int,
                              title: Option[String],
                              price: Price,
                              photoUrl: Option[String])
