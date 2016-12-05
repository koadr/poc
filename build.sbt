import org.scalastyle.sbt.ScalastylePlugin

version := "1.0"

scalaVersion := "2.11.8"

lazy val integrationTest = IntegrationTest.extend(Test)

lazy val dockerSettings =
    DockerSettings.dockerDepSettings ++ DockerSettings.dockerFileSettings ++ DockerSettings.dockerImageSettings


lazy val trovimapPOC = (project in file(".")).
  configs(integrationTest).
  enablePlugins(BuildInfoPlugin, DockerPlugin).
  settings(dockerSettings:_*).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.trovimap"
  ).
  settings(parallelExecution in IntegrationTest := false).
  settings( Defaults.itSettings : _*).
  settings(libraryDependencies ++= {
    val akkaHttpV   = "3.0.0-RC1"
    val akkaV   = "2.4.12"
    val loggingVersion = "1.1.7"
    Seq(
      "ch.qos.logback" % "logback-classic" % loggingVersion,
      "ch.qos.logback" % "logback-core" % loggingVersion,
      "org.slf4j" % "slf4j-api" % "1.7.21",
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "it,test",
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
      "com.typesafe.akka" %% "akka-testkit" % akkaV,
      "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.4.0",
      "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "2.4.0",
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
  }).settings(ScalastylePlugin.projectSettings).
  settings(
    assemblyMergeStrategy in assembly := {
      case PathList(ps @ _*) if ps.last endsWith "BaseDateTime.class" => MergeStrategy.filterDistinctLines
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  ).
  settings(coverageEnabled.in(Test, test) := true).
  settings(mainClass := Some("com.trovimap.infrastructure.http.Trovimap")).
  settings(
    name := "trovimap-poc",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

