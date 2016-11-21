package com.trovimap.infrastructure.http.rest

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpec}

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class PingServiceSpec
    extends WordSpec
    with MustMatchers
    with ScalaFutures
    with ScalatestRouteTest {

  val routes = new PingService().routes

  "PingService" should {
    "respond to single ping query" in {
      Get("/ping") ~> routes ~> check {
        status mustBe StatusCodes.OK
        contentType mustBe `text/plain(UTF-8)`
        responseAs[String] mustBe "PONG"
      }

    }
  }

}
