package com.trovimap.infrastructure

import javax.sql.DataSource

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import com.sksamuel.elastic4s.IndexAndType
import com.trovimap.api.PropertyApi
import com.trovimap.infrastructure.http.rest.PropertyService
import com.trovimap.infrastructure.slick.{
  SlickExecutionContext,
  SlickPropertyRepository
}
import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import _root_.slick.jdbc.JdbcBackend.Database
import com.sksamuel.elastic4s.testkit.ElasticSugar

import scala.concurrent.ExecutionContext

trait TestTrovimap extends ElasticSugar {
  val port =
    ConfigFactory.load("application.it.conf").getInt("services.postgres.port")
  private val dbBuilder = EmbeddedPostgres.builder().setPort(0)
  private val database = dbBuilder.start()
  private val datasource = database.getPostgresDatabase

  private val slickEc = new SlickExecutionContext(ExecutionContext.global)
  val db = Database.forDataSource(datasource)
  doSchemaMigration(datasource)

  val config = ConfigFactory.load("application.it.conf")
  val indexAndType = IndexAndType(config.getString("elasticsearch.index"),
                                  config.getString("elasticsearch.type"))

  // Client below is the test elastic search client
  val propertyQueryService =
    new elasticsearch.PropertyQueryService(client, indexAndType)(
      ExecutionContext.global)
  val propertyRepo = new SlickPropertyRepository(db)(slickEc)
  val propertyApi = new PropertyApi(propertyRepo, propertyQueryService)

  val routes = new PropertyService(propertyApi).routes

  def dbHandle[T](fn: => T): T = {
    val res = fn
    database.close()
    res
  }

  private def doSchemaMigration(datasource: DataSource): Unit = {
    val flyway = new Flyway
    flyway.setDataSource(datasource)
    val _ = flyway.migrate()
  }

}
