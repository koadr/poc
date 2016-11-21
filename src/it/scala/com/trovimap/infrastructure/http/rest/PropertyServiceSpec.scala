package com.trovimap.infrastructure.http.rest

import java.net.URLEncoder

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.sksamuel.elastic4s.ElasticDsl._
import com.trovimap.api.data.PropertyData
import com.trovimap.domain.{Property, TestHelpers}
import com.trovimap.infrastructure.TestTrovimap
import com.trovimap.infrastructure.elasticsearch.ElasticSearchSpec
import com.trovimap.infrastructure.http.dto.PropertyDTO
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpecLike}

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class PropertyServiceSpec
    extends ElasticSearchSpec
    with WordSpecLike
    with MustMatchers
    with ScalaFutures
    with ScalatestRouteTest {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._
  import com.trovimap.infrastructure.http.protocols.Protocols._

  "PropertyService" should {
    "respond with a 404 if property does not exist" in new TestTrovimap {
      dbHandle {
        Get("/properties/test") ~> routes ~> check {
          status mustBe StatusCodes.NotFound
        }
      }
    }

    "respond to single property query if exists" in new TestTrovimap
    with TestHelpers {
      dbHandle {
        val property = arbProperty
        val createProp = propertyRepo.createProperty(property).futureValue
        Get(s"/properties/${property.id.id}") ~> routes ~> check {
          status mustBe StatusCodes.OK
          contentType mustBe `application/json`
          responseAs[PropertyDTO] mustBe dto(property)
        }
      }
    }

    "successfully creates a property" in new TestTrovimap with TestHelpers {
      dbHandle {
        val property = arbProperty
        Post("/properties", dto(property)) ~> routes ~> check {
          val statusEq = status mustBe StatusCodes.OK
          val contentTypeEq = contentType mustBe `application/json`
          val responseEq = responseAs[PropertyDTO] mustBe dto(property)
        }
      }
    }

    "provide ability to search properties" in new TestTrovimap
    with TestHelpers {
      dbHandle {
        val numberOfBathrooms = 3
        val property =
          PropertyData(arbString,
                       arbString,
                       None,
                       numberOfBathrooms,
                       6,
                       None,
                       arbPrice,
                       None)
        val res = client execute {
          indexInto(indexAndType)
            .source(property)
            .id(property.id)
            .refresh(true)
        } await ()

        val filter = "filter[numberOfBathrooms]"
        val url = "/properties?" + URLEncoder
            .encode(filter, "UTF-8") + numberOfBathrooms
        Get(url) ~> routes ~> check {
          val statusEq = status mustBe StatusCodes.OK
          val contentTypeEq = contentType mustBe `application/json`
          val responseEq = responseAs[Seq[PropertyData]] mustBe Seq(property)
        }
      }
    }
  }

  private def dto(property: Property): PropertyDTO = {
    PropertyDTO(
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
}
