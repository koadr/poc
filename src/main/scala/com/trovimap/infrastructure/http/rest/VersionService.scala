package com.trovimap.infrastructure.http.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.trovimap.BuildInfo

class VersionService {

  val routes: Route =
    get {
      path("version") {
        complete(BuildInfo.toString)
      }
    }
}
