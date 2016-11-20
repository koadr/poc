package com.trovimap.infrastructure.slick

import scala.concurrent.ExecutionContext

class SlickExecutionContext(ec: ExecutionContext) extends ExecutionContext {
  override def execute(runnable: Runnable): Unit = ec.execute(runnable)

  override def reportFailure(cause: Throwable): Unit = ec.reportFailure(cause)
}
