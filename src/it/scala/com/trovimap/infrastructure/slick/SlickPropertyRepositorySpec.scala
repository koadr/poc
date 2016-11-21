package com.trovimap.infrastructure.slick

import com.trovimap.domain.TestHelpers
import com.trovimap.infrastructure.TestTrovimap
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Assertion, MustMatchers, WordSpec}

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class SlickPropertyRepositorySpec
    extends WordSpec
    with MustMatchers
    with ScalaFutures {

  import scala.concurrent.duration._
  implicit val defaultPatience =
    PatienceConfig(timeout = 3.seconds, interval = 2.millis)

  "getProperty()" should {
    "fetch property that exists" in new TestTrovimap with TestHelpers {
      val property = arbProperty
      val attemptCreate = propertyRepo.createProperty(property)
      val attemptGet = propertyRepo.getPropertyById(property.id)

      whenReady(attemptGet) { retrievedProperty =>
        retrievedProperty mustBe Some(property)
      }
    }

    "not fetch property that does not exist" in new TestTrovimap with TestHelpers {
      val property = arbProperty
      val attemptGet = propertyRepo.getPropertyById(property.id)

      whenReady(attemptGet) { retrievedProperty =>
        retrievedProperty mustBe None
      }
    }

  }

  "createProperty()" should {
    "create new property correctly" in new TestTrovimap with TestHelpers {
      val property = arbProperty
      val attemptCreate = propertyRepo.createProperty(property)

      whenReady(attemptCreate) { createdProperty =>
        createdProperty mustBe Some(property)
      }
    }

  }

  "updateProperty()" should {
    "update new property correctly" in new TestTrovimap with TestHelpers {
      val property = arbProperty
      val attemptCreate = propertyRepo.createProperty(property)
      val adjustedPrice = arbPrice
      val adjustedProperty = property.associatePrice(adjustedPrice)
      val attempUpdate = propertyRepo.updateProperty(adjustedProperty)

      whenReady(attempUpdate) { updatedProperty =>
        updatedProperty mustBe Some(adjustedProperty)
      }
    }

  }

}
