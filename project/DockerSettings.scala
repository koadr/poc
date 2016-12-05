import _root_.sbt.Keys._
import _root_.sbt._
import sbtassembly.AssemblyKeys._
import sbtdocker.DockerKeys._
import sbtdocker.mutable.Dockerfile
import sbtdocker.{DockerfileLike, ImageId, ImageName}


object DockerSettings {

  val dockerRegistry = "koadr"
  val name = "poc"
  lazy val dockerDepSettings: _root_.sbt.Def.Setting[Task[ImageId]] = docker <<= docker.dependsOn(assembly)
  lazy val port = 9001

  lazy val dockerFileSettings: _root_.sbt.Def.Setting[Task[DockerfileLike]] =
    dockerfile in docker := {
      val artifact = (assemblyOutputPath in assembly).value
      val artifactName = (assemblyDefaultJarName in assembly).value
      val appDirPath = "/opt/docker"
      val artifactTargetPath = s"$appDirPath/$artifactName"
      val main = mainClass.value.getOrElse("com.trovimap.infrastructure.http.Trovimap")

      new Dockerfile {
        from("openjdk:8")
        add(artifact, artifactTargetPath)
        expose(port)
        workDir(appDirPath)
        entryPointShell("exec", "java", "$JAVA_OPTS", "-cp", artifactTargetPath, main)
      }
    }

  lazy val dockerImageSettings = imageNames in docker := Seq(
    // Sets the latest tag
    ImageName(s"$dockerRegistry/$name:latest"),

    // Sets a name with a tag that contains the project version
    ImageName(
      registry = Some(dockerRegistry),
      repository = name,
      tag = Some("v" + s"${version.value}-${Option(System.getenv("BUILD_NUMBER")).getOrElse("0")}")
    )
  )
}