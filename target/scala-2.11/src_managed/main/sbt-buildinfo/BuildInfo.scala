package com.trovimap

import scala.Predef._

/** This object was generated by sbt-buildinfo. */
case object BuildInfo {
  /** The value is "trovimap-poc". */
  val name: String = "trovimap-poc"
  /** The value is "1.0". */
  val version: String = "1.0"
  /** The value is "2.11.8". */
  val scalaVersion: String = "2.11.8"
  /** The value is "0.13.8". */
  val sbtVersion: String = "0.13.8"
  override val toString: String = {
    "name: %s, version: %s, scalaVersion: %s, sbtVersion: %s" format (
      name, version, scalaVersion, sbtVersion
    )
  }
}
