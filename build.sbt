import sbt.Keys._
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.playcrosscompilation.PlayVersion.{Play25, Play26}
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion


val appName = "play-language"

lazy val playLanguage = (project in file("."))
  .enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    majorVersion := 4,
    name := appName,
    scalaVersion := "2.11.12",
    crossScalaVersions := Seq("2.11.12", "2.12.10"),
    libraryDependencies ++= AppDependencies.all,
    resolvers += Resolver.typesafeRepo("releases"),
    resolvers += Resolver.bintrayRepo("hmrc", "releases"),
    makePublicallyAvailableOnBintray := true,
    PlayKeys.playPlugin := true
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings,
    sourceDirectories in(Compile, TwirlKeys.compileTemplates) += {
      PlayCrossCompilation.playVersion match {
        case Play25 => (sourceDirectory in Compile).value / "play-25"
        case Play26 => (sourceDirectory in Compile).value / "play-26"
      }
    }
  )


