import play.{PlayImport, PlayScala}
import play.core.PlayVersion
import sbt.Keys._
import sbt._
import uk.gov.hmrc._
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  import AppDependencies._

  val appName = "play-language"

  lazy val playLanguage = (project in file("."))
    .enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning)
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

  object Compile {

    private val frontendBootstrapVersion = "1.1.0"

    val playFramework     = "com.typesafe.play" %% "play" % PlayVersion.current % "provided"
    val playWS            = PlayImport.ws % "provided"
    val frontendBootstrap = "uk.gov.hmrc" %% "frontend-bootstrap" % frontendBootstrapVersion

  }

  sealed abstract class Test(scope: String) {

    private val scalaTestVersion  = "2.2.4"
    private val pegdownVersion    = "1.4.2"

    val playTest  = "com.typesafe.play" %% "play-test"   % PlayVersion.current % scope
    val scalaTest = "org.scalatest"     %% "scalatest"   % scalaTestVersion    % scope
    val pegdown   = "org.pegdown"       %  "pegdown"     % pegdownVersion      % scope

  }

  object Test extends Test("test")
}
