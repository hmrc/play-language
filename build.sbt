import play.sbt.PlayScala
import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

val appName = "play-language"

lazy val playLanguage = (project in file("."))
  .enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning)
  .settings(
    majorVersion := 3,
    name := appName,
    scalaVersion := "2.11.12",
    crossScalaVersions := Seq("2.11.12"),
    libraryDependencies ++= AppDependencies.compile() ++ AppDependencies.test(),
    resolvers += Resolver.typesafeRepo("releases"),
    resolvers += Resolver.bintrayRepo("hmrc", "releases")
  )

