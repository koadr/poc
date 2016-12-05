package com.trovimap.infrastructure.http

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri, IndexAndType}
import com.trovimap.api.PropertyApi
import com.trovimap.infrastructure.http.rest.{PingService, PropertyService, VersionService}
import com.trovimap.infrastructure.slick.{SlickExecutionContext, SlickPropertyRepository}
import com.typesafe.config.ConfigFactory
import com.trovimap.infrastructure.elasticsearch
import com.trovimap.infrastructure.inmem.InMemoryPropertyRepository

object Trovimap extends App {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val config = ConfigFactory.load()
  implicit val logger = Logging(system, getClass)

  private val host = config.getString("http.interface")
  private val port = config.getInt("http.port")
  private val db: slick.jdbc.JdbcBackend.Database =
    slick.jdbc.JdbcBackend.Database.forConfig(path = "db", config)

  // Custom execution context for slick
  val slickEC = new SlickExecutionContext(
    system.dispatchers.lookup("db.dispatcher"))
  val propertyRepo = new InMemoryPropertyRepository()

  // Elasticsearch
  import org.elasticsearch.common.settings.Settings
  val clusterConfig = config.getConfig("services.elasticsearch")
  val clusterName = clusterConfig.getString("name")
  val clusterHost = clusterConfig.getString("host")
  val clusterPort = clusterConfig.getInt("port")
  val settings = Settings.builder().put("cluster.name", clusterName).build()
  val client = ElasticClient
    .transport(settings, ElasticsearchClientUri(clusterHost, clusterPort))

  val elasticSearchEC = new SlickExecutionContext(
    system.dispatchers.lookup("elasticsearch.dispatcher"))
  val indexAndType = IndexAndType(config.getString("elasticsearch.index"),
                                  config.getString("elasticsearch.type"))
  val propertyQueryService =
    new elasticsearch.PropertyQueryService(client, indexAndType)(
      elasticSearchEC)

  val propertyApi = new PropertyApi(propertyRepo, propertyQueryService)

  val routes =
    new PingService().routes ~ new PropertyService(propertyApi).routes ~ new VersionService().routes

  val bindingFuture = Http().bindAndHandle(routes, host, port)

  bindingFuture.onFailure {
    case ex: Exception =>
      logger.error(ex, "Failed to bind to {}:{}!", host, port)
  }
}
