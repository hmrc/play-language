resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)
resolvers += Resolver.typesafeRepo("releases")

val playPlugin = sys.env.getOrElse("PLAY_VERSION", "2.6") match {
  case "2.6" => "com.typesafe.play" % "sbt-plugin" % "2.6.25"
  case "2.7" => "com.typesafe.play" % "sbt-plugin" % "2.7.9"
  case "2.8" => "com.typesafe.play" % "sbt-plugin" % "2.8.7"
}

addSbtPlugin(playPlugin)
addSbtPlugin("uk.gov.hmrc"   % "sbt-auto-build"             % "3.0.0")
addSbtPlugin("uk.gov.hmrc"   % "sbt-play-cross-compilation" % "2.2.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt"               % "2.4.0")
