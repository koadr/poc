logLevel := Level.Warn

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.2.1")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.0.3")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

resolvers += "Flyway" at "https://flywaydb.org/repo"