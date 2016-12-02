package com.trovimap.infrastructure.http.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server._
import com.trovimap.api.PropertyApi
import com.trovimap.domain.{Property, PropertyId}
import com.trovimap.infrastructure.http.dto.PropertyDTO

import scala.concurrent.Future

class PropertyService(api: PropertyApi) {
  import com.trovimap.infrastructure.http.protocols.Protocols._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

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

  val routes: Route =
    pathPrefix("properties") {
      get {
        pathEnd {
          parameters("filter[numberOfBedrooms]".as[Int] ?,
                     "filter[numberOfBathrooms]".as[Int] ?,
                     "filter[id]" ?) {
            (numberOfBedrooms, numberOfBathrooms, id) =>
              val attributes = Map("numberOfBedrooms" -> numberOfBedrooms,
                                   "numberOfBathrooms" -> numberOfBathrooms,
                                   "id" -> id)
              val properties = attributes.foldLeft(api.properties) {
                case (builder, ("numberOfBedrooms", Some(value: Int))) =>
                  builder.withBedrooms(value)
                case (builder, ("numberOfBathrooms", Some(value: Int))) =>
                  builder.withBathrooms(value)
                case (builder, ("id", Some(value: String))) =>
                  builder.withId(value)
                case (builder, _) => builder
              }
              val maybeProperties = properties.search()
              onSuccess(maybeProperties) { complete(_) }
          }
        }
      } ~
        (get & path(Segment)) { id =>
          val maybeProperty: Future[Option[Property]] =
            api.getProperty(PropertyId(id))
          onSuccess(maybeProperty) {
            case Some(property) => complete(dto(property))
            case None => complete(StatusCodes.NotFound)
          }
        } ~ (post & entity(as[PropertyDTO])) { body =>
        val maybeProperty = api.createProperty(body.property)
        onSuccess(maybeProperty) {
          case Some(property) => complete(dto(property))
          case None => complete(StatusCodes.BadRequest)
        }

      }
    }

}
