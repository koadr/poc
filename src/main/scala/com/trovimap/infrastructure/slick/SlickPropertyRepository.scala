package com.trovimap.infrastructure.slick

import com.trovimap.domain
import com.trovimap.domain.{PropertyId, PropertyRepository}
import com.trovimap.infrastructure.slick.tables.PropertyTable

import scala.concurrent.Future

object SlickPropertyRepository {
  final class PropertyNotFoundException(msg: String) extends Exception(msg)
  trait Error {
    def detail: String
  }
  final case class PropertyNotFound(detail: String) extends Error
}

class SlickPropertyRepository(db: slick.jdbc.JdbcBackend.Database)(
    implicit executionContext: SlickExecutionContext)
    extends PropertyRepository {
  import PropertyTable._
  import SlickPropertyRepository._
  import TrovimapPostgresDriver.api._

  /**
    * Serves as another example of access patterns in which the underlying item may not exist. This
    * pattern is usually discouraged as throw "implies partialitiy". In fact, the static analysis tool - WartRemover -
    * will not compile without the @SuppressWarnings below.
    *
    * @param id the id of the property
    * @throws PropertyNotFoundException if the property doesn't not exist
    * @return the specified property
    */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  @throws[PropertyNotFoundException]
  private def _getPropertyByIdException(
      id: PropertyId): Future[domain.Property] = {
    val result: DBIO[domain.Property] =
      Property.filter(_.id === id).result.headOption.map {
        case Some(row) => row.property
        case None => throw new PropertyNotFoundException("Property not found")
      }
    db.run(result)
  }

  /**
    * The exceptional example above is frowned upon. It is
    * recommended to encode exceptions/errors as eithers as the below example shows
    *
    * @param id
    * @return the specified property or an error if it could not be found
    */
  private def _getPropertyByIdEither(
      id: PropertyId): Future[Either[Error, domain.Property]] = {
    val result: DBIO[Either[PropertyNotFound, domain.Property]] =
      Property.filter(_.id === id).result.headOption.map {
        case Some(row) => Right(row.property)
        case None => Left(PropertyNotFound("Property not found"))
      }
    db.run(result)
  }

  /**
    * An example of how to run multiple statements together in a transaction.
    * You have to deal only with DBIOS, compose them, and at the end you call .transactionally
    * which runs all queries within the transaction. Ideally I would make this return an either and not
    * fail with an exception but didn't want to complicate it more as all i'm showcasing is running multiple
    * statements within a transaction.
    * @param id
    * @return the specified property created
    */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  @throws[PropertyNotFoundException]
  private def _runningMultipleInTransaction(id: PropertyId) = {
    val result = (for {
      prop <- getPropertyByIdDBIO(id)
      _ <- prop
        .map(createPropertyDBIO)
        .getOrElse(
          DBIO.failed(new PropertyNotFoundException("Property not found")))
    } yield prop).transactionally

    db.run(result)
  }

  private def getPropertyByIdDBIO(
      id: PropertyId): DBIO[Option[domain.Property]] = {
    Property.filter(_.id === id).result.headOption.map { _.map(_.property) }
  }

  private def createPropertyDBIO(property: domain.Property): DBIO[Int] = {
    Property += row(property)
  }

  override def getPropertyById(
      id: PropertyId): Future[Option[domain.Property]] = {
    db.run(getPropertyByIdDBIO(id))
  }

  override def createProperty(
      property: domain.Property): Future[Option[domain.Property]] = {
    val result = createPropertyDBIO(property)
    db.run(result.map(_ => Some(property)).transactionally)
  }

  override def updateProperty(
      property: domain.Property): Future[Option[domain.Property]] = {
    val result = db.run(
      Property
        .filter(_.id === property.id)
        .update(row(property))
        .transactionally)
    result.map(_ => Some(property))
  }
}
