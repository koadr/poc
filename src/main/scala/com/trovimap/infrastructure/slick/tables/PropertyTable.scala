package com.trovimap.infrastructure.slick.tables

import java.net.URL
import java.util.Currency

import com.trovimap.domain.{BrokerId, PropertyId}
import com.trovimap.domain

object PropertyTable extends PropertyTable

private[tables] trait PropertyTable {
  import com.trovimap.infrastructure.slick.TrovimapPostgresDriver.api._

  def row(property: domain.Property): PropertyRow =
    PropertyRow(
      property.id,
      property.brokerId,
      property.location.map(_.latitude),
      property.location.map(_.longitude),
      property.numberOfBathrooms,
      property.numberOfBedrooms,
      property.title,
      property.price.cents,
      property.price.currency,
      property.photoUrl
    )

  case class PropertyRow(
      id: PropertyId,
      brokerId: BrokerId,
      locationLatitude: Option[Long],
      locationLongitude: Option[Long],
      numberOfBathrooms: Int,
      numberOfBedrooms: Int,
      title: Option[String],
      priceAmount: Int,
      priceCurrency: Currency,
      photoUrl: Option[URL]
  ) {
    private def location(longitude: Option[Long],
                         latitude: Option[Long]): Option[domain.Location] =
      for {
        lon <- longitude
        lat <- latitude
      } yield domain.Location(lon, lat)

    def property = domain.Property(
      id,
      brokerId,
      location(locationLatitude, locationLongitude),
      numberOfBathrooms,
      numberOfBedrooms,
      title,
      domain.Price(priceAmount, priceCurrency),
      photoUrl
    )
  }

  implicit val propertyIdColumnType =
    MappedColumnType.base[PropertyId, String](
      propertyId => propertyId.id,
      PropertyId
    )

  implicit val brokerIdColumnType = MappedColumnType.base[BrokerId, String](
    brokerId => brokerId.id,
    BrokerId
  )

  implicit val urlColumnType = MappedColumnType.base[URL, String](
    url => url.toString,
    s => new URL(s)
  )

  implicit val currencyColumnType = MappedColumnType.base[Currency, String](
    currency => currency.getCurrencyCode,
    s => Currency.getInstance(s.toUpperCase)
  )

  class Property(tag: Tag) extends Table[PropertyRow](tag, "property") {
    def id = column[PropertyId]("id", O.PrimaryKey)
    def brokerId = column[BrokerId]("broker_id")
    def locationLatitude = column[Option[Long]]("location_latitude")
    def locationLongitude = column[Option[Long]]("location_longitude")
    def numberOfBathrooms = column[Int]("number_of_bathrooms")
    def numberOfBedrooms = column[Int]("number_of_bedrooms")
    def title = column[Option[String]]("title")
    def priceAmount = column[Int]("price_amount")
    def priceCurrency = column[Currency]("price_currency")
    def photoUrl = column[Option[URL]]("photo_url")

    def * =
      (id,
       brokerId,
       locationLatitude,
       locationLongitude,
       numberOfBathrooms,
       numberOfBedrooms,
       title,
       priceAmount,
       priceCurrency,
       photoUrl) <> (PropertyRow.tupled, PropertyRow.unapply)
  }
  val Property = TableQuery[Property]

}
