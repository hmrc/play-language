resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

addSbtPlugin("uk.gov.hmrc"       % "sbt-auto-build"             % "3.24.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt"               % "2.4.0")

sys.env.get("PLAY_VERSION") match {
  case Some("2.9") => addSbtPlugin("com.typesafe.play"       % "sbt-twirl"  % "1.6.10")
  case _           => addSbtPlugin("org.playframework.twirl" % "sbt-twirl"  % "2.0.9")
}
