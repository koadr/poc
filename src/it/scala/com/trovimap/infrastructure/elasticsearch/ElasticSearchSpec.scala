package com.trovimap.infrastructure.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.testkit.ElasticSugar
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.concurrent.duration._

trait ElasticSearchSpec extends ElasticSugar with Suite with BeforeAndAfterAll with ScalaFutures {
  private implicit val patienceTimeout = org.scalatest.concurrent.PatienceConfiguration.Timeout(10.seconds)

  override protected def beforeAll(): Unit = {
    super.beforeAll()
  }

  override protected def afterAll(): Unit = {
    client.close()
    super.afterAll()
  }

  protected def dropAllIndexes(): Unit = {
    val _ = client.execute {
      delete index "_all"
    }.await()
  }
}
