resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

addSbtPlugin("uk.gov.hmrc"       % "sbt-auto-build"             % "3.13.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt"               % "2.4.0")

// previously used play sbt-plugin and enabled PlayScala and disabled PlayLayout - this was overkill to add templateImports, and added lots of unnecessary dependencies to created binary (incl. Main-Class config in Manifest)

libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always // // required since we're cross building for Play 2.8 which isn't compatible with sbt 1.9

// we _can_ get away with just using Play 2.8 twirl plugin since the generated code is compatible, and the twirl-api dependency is evicted
addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.5.1")

// however, more correctly (requiring running the build multiple times with specified env var):

/*
sys.env.get("PLAY_VERSION") match {
  case Some("2.9") => addSbtPlugin("com.typesafe.play" % "sbt-twirl"  % "1.6.1")
  case _ /*Some("2.8")*/ => addSbtPlugin("com.typesafe.sbt"  % "sbt-twirl"  % "1.5.1")
}
*/
