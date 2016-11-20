package com.trovimap.infrastructure.elasticsearch

import java.util.Currency

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{
  ElasticClient,
  ElasticsearchClientUri,
  IndexAndType
}
import com.trovimap.api.data.PropertyData
import com.trovimap.domain.Price
import com.trovimap.infrastructure.elasticsearch
import com.trovimap.infrastructure.slick.SlickExecutionContext
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object Bootstrap extends App {
  val config = ConfigFactory.load()

  import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._

  // Elasticsearch
  import org.elasticsearch.common.settings.Settings
  val clusterConfig = config.getConfig("services.elasticsearch")
  val clusterName = clusterConfig.getString("name")
  val clusterHost = clusterConfig.getString("host")
  val clusterPort = clusterConfig.getInt("port")
  val settings = Settings.builder().put("cluster.name", clusterName).build()
  val client = ElasticClient
    .transport(settings, ElasticsearchClientUri(clusterHost, clusterPort))

  val elasticSearchEC = new SlickExecutionContext(ExecutionContext.global)
  val indexAndType = IndexAndType(config.getString("elasticsearch.index"),
                                  config.getString("elasticsearch.type"))
  val propertyQueryService =
    new elasticsearch.PropertyQueryService(client, indexAndType)(
      elasticSearchEC)

  val propertyDataA = PropertyData("a",
                                   "a",
                                   None,
                                   3,
                                   5,
                                   Some("a description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataB = PropertyData("b",
                                   "a",
                                   None,
                                   2,
                                   8,
                                   Some("b description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataC = PropertyData("c",
                                   "a",
                                   None,
                                   5,
                                   3,
                                   Some("c description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataD = PropertyData("d",
                                   "a",
                                   None,
                                   3,
                                   6,
                                   Some("d description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataE = PropertyData("e",
                                   "a",
                                   None,
                                   3,
                                   2,
                                   Some("e description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataF = PropertyData("f",
                                   "a",
                                   None,
                                   3,
                                   5,
                                   Some("f description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataG = PropertyData("g",
                                   "a",
                                   None,
                                   8,
                                   5,
                                   Some("g description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataH = PropertyData("h",
                                   "a",
                                   None,
                                   2,
                                   5,
                                   Some("h description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)
  val propertyDataI = PropertyData("i",
                                   "a",
                                   None,
                                   1,
                                   5,
                                   Some("i description"),
                                   Price(23456, Currency.getInstance("USD")),
                                   None)

  /**
    * Await only called here because we want to have seed data. In production code this should never be called
    */
  val res = client execute {
    bulk(
      indexInto(indexAndType).source(propertyDataA).id(propertyDataA.id),
      indexInto(indexAndType).source(propertyDataB).id(propertyDataB.id),
      indexInto(indexAndType).source(propertyDataC).id(propertyDataC.id),
      indexInto(indexAndType).source(propertyDataD).id(propertyDataD.id),
      indexInto(indexAndType).source(propertyDataE).id(propertyDataE.id),
      indexInto(indexAndType).source(propertyDataF).id(propertyDataF.id),
      indexInto(indexAndType).source(propertyDataG).id(propertyDataG.id),
      indexInto(indexAndType).source(propertyDataH).id(propertyDataH.id),
      indexInto(indexAndType).source(propertyDataI).id(propertyDataI.id)
    ).refresh(true)
  } await ()

}
