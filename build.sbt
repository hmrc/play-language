import sbt.Keys._

val scala2_12 = "2.12.15"
val scala2_13 = "2.13.7"

val silencerVersion = "1.7.7"

lazy val playLanguage = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    majorVersion := 5,
    name := "play-language",
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13),
    PlayCrossCompilation.playCrossCompilationSettings,
    libraryDependencies ++= AppDependencies.all,
    isPublicArtefact := true,
    scalacOptions += "-P:silencer:pathFilters=views",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    )
  )
  .settings(
    Compile / TwirlKeys.compileTemplates / sourceDirectories +=
      (Compile / sourceDirectory).value / "scala"
  )
