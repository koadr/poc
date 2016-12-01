version := "1.0"

scalaVersion := "2.11.8"

lazy val integrationTest = IntegrationTest.extend(Test)

lazy val trovimapPOC = (project in file(".")).
  configs(integrationTest).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.trovimap"
  ).
  settings(parallelExecution in IntegrationTest := false).
  settings(addCommandAlias("it", "it:test")).
  settings( Defaults.itSettings : _*).
  settings(libraryDependencies ++= {
    val akkaHttpV   = "3.0.0-RC1"
    val akkaV   = "2.4.12"
    Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "it,test",
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
      "com.typesafe.akka" %% "akka-testkit" % akkaV,
      "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.4.0" exclude("joda-time", "joda-time"),
      "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "2.4.0" exclude("joda-time", "joda-time"),
      "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "2.4.0" % "it,test",
      "com.typesafe.slick" %% "slick" % "3.1.1",
      "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
      "com.typesafe.slick" % "slick-hikaricp_2.11" % "3.1.1",
      "com.github.tminglei" %% "slick-pg" % "0.14.3",
      "org.scalactic" %% "scalactic" % "3.0.0",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.flywaydb" %% "flyway-play" % "3.0.1" % "it,test",
      "com.opentable.components" % "otj-pg-embedded" % "0.7.1" % "it,test"
    )
  }).
  settings(wartremoverErrors ++= Warts.unsafe).
  settings(wartremoverWarnings ++= Warts.unsafe).
  settings(
    name := "trovimap-poc",
    version := "1.0",
    scalaVersion := "2.11.8"
  )
