package com.trovimap.domain

import java.util.Currency

import org.scalatest._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class PropertySpec extends WordSpec with MustMatchers {

  "Property" should {
    "allow price updates" in new TestHelpers {
      val id = arbPropertyId
      val brokerId = arbBrokerId
      val location = arbLocation
      val price = arbPrice
      val discountedPrice = Price(15000, Currency.getInstance("USD"))
      val property = arbProperty

      property.associatePrice(discountedPrice).price mustBe discountedPrice
    }
  }
}
