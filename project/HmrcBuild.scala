import _root_.play.core.PlayVersion
import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc._
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  import uk.gov.hmrc.DefaultBuildSettings
  import DefaultBuildSettings._
  import play.core.PlayVersion
  import play.PlayImport._
  import AppDependencies._

  val appName = "play-language"

  lazy val playLangauge = (project in file("."))
    .enablePlugins(play.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      name := appName,
      scalaVersion := "2.11.7",
      crossScalaVersions := Seq("2.11.7"),
      libraryDependencies ++= Seq(
        Compile.playFramework,
        Compile.playWS,
        Compile.frontendBootstrap,
        Test.playTest,
        Test.scalaTest,
        Test.pegdown
      )
    )

  resolvers += Resolver.typesafeRepo("releases")
  resolvers += Resolver.bintrayRepo("hmrc", "releases")
    
}

private object AppDependencies {

  import play.PlayImport._
  import play.core.PlayVersion

  object Compile {
    
    private val frontendBootstrapVersion = "1.1.0"

    val playFramework     = "com.typesafe.play" %% "play" % PlayVersion.current % "provided"
    val playWS            = ws % "provided"
    val frontendBootstrap = "uk.gov.hmrc" %% "frontend-bootstrap" % frontendBootstrapVersion
  }

  sealed abstract class Test(scope: String) {

    private val scalaTestVersion  = "2.2.4"
    private val pegdownVersion    = "1.4.2"
    private val mockitoVersion    = "1.9.5"
    private val httpVerbsVersion  = "3.0.0"

    val playTest  = "com.typesafe.play" %% "play-test"   % PlayVersion.current % scope
    val scalaTest = "org.scalatest"     %% "scalatest"   % scalaTestVersion    % scope
    val pegdown   = "org.pegdown"       %  "pegdown"     % pegdownVersion      % scope

  }

  object Test extends Test("test")
}
