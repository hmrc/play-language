import sbt.Keys._
import scala.collection.JavaConverters._

val scala2_13 = "2.13.12"
val scala3    = "3.3.3"

ThisBuild / majorVersion := 7
ThisBuild / isPublicArtefact := true
ThisBuild / scalaVersion := scala2_13
ThisBuild / scalacOptions += "-Wconf:src=views/.*:s"
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always // required since we're cross building for Play 2.8 which isn't compatible with sbt 1.9

lazy val projects: Seq[ProjectReference] =
  sys.env.get("PLAY_VERSION") match {
    case Some("2.8") => Seq(playLanguage, playLanguagePlay28)
    case Some("2.9") => Seq(playLanguagePlay29)
    case _           => Seq(playLanguagePlay30)
  }

lazy val library = (project in file("."))
  .settings(publish / skip := true)
  .aggregate(
    projects: _*
  )

// empty artefact, exists to ensure eviction of previous play-language jar which has now moved into play-language-play-28
lazy val playLanguage = Project("play-language", file("play-language"))

def copySources(module: Project) = Seq(
  Compile / scalaSource := (module / Compile / scalaSource).value,
  Compile / resourceDirectory := (module / Compile / resourceDirectory).value,
  Test / scalaSource := (module / Test / scalaSource).value,
  Test / resourceDirectory := (module / Test / resourceDirectory).value
)

lazy val playLanguagePlay28 = Project("play-language-play-28", file("play-language-play-28"))
  .enablePlugins(
    SbtTwirl
  ) // previously used play sbt-plugin and enabled PlayScala and disabled PlayLayout - this was overkill to add templateImports, and added lots of unnecessary dependencies to created binary (incl. Main-Class config in Manifest)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(copySources(playLanguagePlay30))
  .settings(
    libraryDependencies ++= AppDependencies.play28
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
  .dependsOn(playLanguage)

lazy val playLanguagePlay29 = Project("play-language-play-29", file("play-language-play-29"))
  .enablePlugins(SbtTwirl)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(copySources(playLanguagePlay30))
  .settings(
    libraryDependencies ++= AppDependencies.play29
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

lazy val playLanguagePlay30 = Project("play-language-play-30", file("play-language-play-30"))
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
