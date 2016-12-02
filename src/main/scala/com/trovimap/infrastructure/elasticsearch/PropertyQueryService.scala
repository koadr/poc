package com.trovimap.infrastructure.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{
  ElasticClient,
  IndexAndType,
  MatchQueryDefinition,
  RangeQueryDefinition
}
import com.trovimap.api
import com.trovimap.api.data.PropertyData
import com.trovimap.domain.Property.Attributes

import scala.concurrent.{ExecutionContext, Future}

class PropertyQueryService(client: ElasticClient, indexAndType: IndexAndType)(
    implicit ec: ExecutionContext)
    extends api.PropertyQueryService {
  import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._


  override def searchByAttributes(
      attributes: Attributes): Future[Seq[PropertyData]] = {
    client execute {
      search in indexAndType query bool {
        must(
          attributes.foldLeft(Seq.empty[MatchQueryDefinition]) {
            case (acc, (field, value)) =>
              matchQuery(field, value) +: acc
          }
        )
      }
    } map (_.as[PropertyData])
  }

}
