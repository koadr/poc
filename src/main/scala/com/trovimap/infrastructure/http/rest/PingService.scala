package com.trovimap.infrastructure.http.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

class PingService {

  val routes: Route =
    get {
      path("ping") {
        complete(StatusCodes.OK, "PONG")
      }
    }
}
