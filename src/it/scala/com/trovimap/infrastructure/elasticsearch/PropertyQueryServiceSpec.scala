package com.trovimap.infrastructure.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.trovimap.api.data.PropertyData
import com.trovimap.domain.{Property, TestHelpers}
import com.trovimap.infrastructure.TestTrovimap
import org.scalatest.{MustMatchers, WordSpecLike}


class PropertyQueryServiceSpec
    extends ElasticSearchSpec
    with WordSpecLike
    with MustMatchers {
  import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._

  private def propertyData(property: Property): PropertyData = {
    PropertyData(
      property.id.id,
      property.brokerId.id,
      property.location,
      property.numberOfBathrooms,
      property.numberOfBedrooms,
      property.title,
      property.price,
      property.photoUrl.map(_.toString)
    )
  }

  "PropertyQueryService" should {
    "query based on a single attribute" in new TestTrovimap with TestHelpers {
      val propertya =
        propertyData(arbProperty.copy(numberOfBathrooms = 3)).copy(id = "a")
      val propertyb =
        propertyData(arbProperty.copy(numberOfBathrooms = 3)).copy(id = "b")

      client execute {
        bulk(
          indexInto(indexAndType).source(propertya).id(propertya.id),
          indexInto(indexAndType).source(propertyb).id(propertyb.id)
        ).refresh(true)
      } await ()

      val Seq(propa, propb) = propertyQueryService
        .searchByAttributes(Map("numberOfBathrooms" -> 3))
        .futureValue
        .sortBy(_.id)
      propa must be(propertya)
      propb must be(propertyb)
    }

    "query based on multiple attributes" in new TestTrovimap with TestHelpers {
      val propertya = propertyData(
        arbProperty.copy(numberOfBathrooms = 3, numberOfBedrooms = 5))
        .copy(id = "a")
      val propertyb = propertyData(
        arbProperty.copy(numberOfBathrooms = 3, numberOfBedrooms = 6))
        .copy(id = "b")

      client execute {
        bulk(
          indexInto(indexAndType).source(propertya).id(propertya.id),
          indexInto(indexAndType).source(propertyb).id(propertyb.id)
        ).refresh(true)
      } await ()

      val Seq(propb) = propertyQueryService
        .searchByAttributes(
          Map("numberOfBathrooms" -> 3, "numberOfBedrooms" -> 6))
        .futureValue
      propb must be(propertyb)
    }
  }

}
