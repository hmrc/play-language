import sbt.Keys._
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

val appName = "play-language"

val silencerVersion = "1.7.3"

lazy val playLanguage = (project in file("."))
  .enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    majorVersion := 4,
    name := appName,
    scalaVersion := "2.12.13",
    PlayCrossCompilation.playCrossCompilationSettings,
    libraryDependencies ++= AppDependencies.all,
    makePublicallyAvailableOnBintray := true,
    scalacOptions += "-P:silencer:pathFilters=views",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    )
  )
  .settings(
    sourceDirectories in (Compile, TwirlKeys.compileTemplates) +=
      (sourceDirectory in Compile).value / "scala"
  )
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
