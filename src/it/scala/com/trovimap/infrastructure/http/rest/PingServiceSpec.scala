package com.trovimap.infrastructure.http.rest

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpec}

class PingServiceSpec
    extends WordSpec
    with MustMatchers
    with ScalaFutures
    with ScalatestRouteTest {

  val routes = new PingService().routes

  "PingService" should {
    "respond to single ping query" in {
      Get("/ping") ~> routes ~> check {
        val statusEq = status mustBe StatusCodes.OK
        val contentTypeEq = contentType mustBe `text/plain(UTF-8)`
        val responseEq = responseAs[String] mustBe  "PONG"
      }

    }
  }

}
