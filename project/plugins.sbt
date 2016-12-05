logLevel := Level.Warn

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.0.3")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")

resolvers += "Flyway" at "https://flywaydb.org/repo"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"