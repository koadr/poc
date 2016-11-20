package com.trovimap.infrastructure.elasticsearch

import scala.concurrent.ExecutionContext

class ElasticSearchExecutionContext(ec: ExecutionContext)
    extends ExecutionContext {
  override def execute(runnable: Runnable): Unit = ec.execute(runnable)

  override def reportFailure(cause: Throwable): Unit = ec.reportFailure(cause)
}
