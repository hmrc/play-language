import sbt.Keys._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion


val appName = "play-language"
val playDir = "play-26"
val scalaVersions = Seq("2.11.12", "2.12.10")

lazy val playLanguage = (project in file("."))
  .enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    majorVersion := 4,
    name := appName,
    scalaVersion := "2.11.12",
    crossScalaVersions := scalaVersions,
    PlayCrossCompilation.playCrossCompilationSettings,
    libraryDependencies ++= AppDependencies.all,
    resolvers += Resolver.typesafeRepo("releases"),
    resolvers += Resolver.bintrayRepo("hmrc", "releases")
  )
  .settings(sourceDirectories in(Compile, TwirlKeys.compileTemplates) +=
    (sourceDirectory in Compile).value / playDir)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
