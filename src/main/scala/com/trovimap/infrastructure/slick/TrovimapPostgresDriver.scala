package com.trovimap.infrastructure.slick

import com.github.tminglei.slickpg._

trait TrovimapPostgresDriver extends ExPostgresDriver {
  override val api = new API {}
}

object TrovimapPostgresDriver extends TrovimapPostgresDriver
