import sbt.Keys._

val scala2_13 = "2.13.18"
val scala3    = "3.3.6"

ThisBuild / majorVersion := 9
ThisBuild / isPublicArtefact := true
ThisBuild / scalaVersion := scala2_13
ThisBuild / scalacOptions += "-Wconf:src=views/.*:s"

lazy val library = Project("play-language-play-30", file("play-language-play-30"))
  .enablePlugins(SbtTwirl)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    crossScalaVersions := Seq(scala2_13, scala3),
    libraryDependencies ++= AppDependencies.play30
  )
  .settings(
    Compile / TwirlKeys.compileTemplates / sourceDirectories ++=
      (Compile / unmanagedSourceDirectories).value,
    TwirlKeys.templateImports ++= Seq(
      "play.api.mvc._",
      "play.api.data._",
      "play.api.i18n._"
    )
  )
