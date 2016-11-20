package com.trovimap.infrastructure.http.protocols

import java.util.Currency

import com.trovimap.api.data.PropertyData
import com.trovimap.domain._
import com.trovimap.infrastructure.http.dto.PropertyDTO
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat}

object Protocols extends Protocols

trait Protocols extends DefaultJsonProtocol {
  implicit val locationFormat = jsonFormat2(Location)

  implicit object CurrencyFormat extends RootJsonFormat[Currency] {
    override def read(json: JsValue): Currency = json match {
      case JsString(code) => Currency.getInstance(code)
    }

    override def write(c: Currency): JsValue = JsString(c.toString)
  }

  implicit val priceFormat = jsonFormat2(Price)
  implicit val propertyFormat = jsonFormat8(PropertyDTO)
  implicit val propertyDataFormat = jsonFormat8(PropertyData)
}
