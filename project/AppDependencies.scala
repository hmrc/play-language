/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt._


object AppDependencies {

  val compile: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    shared = Seq("com.ibm.icu" % "icu4j" % "64.2"),

    play25 = Seq(
      "com.typesafe.play"     %% "play"         % "2.5.19",
      "uk.gov.hmrc"           %% "url-builder"  % "3.4.0-play-25"
    ),
    play26 = Seq(
      "com.typesafe.play"     %% "play"         % "2.6.20",
      "uk.gov.hmrc"           %% "url-builder"  % "3.4.0-play-26"
    ),
    play27 = Seq(
      "com.typesafe.play"     %% "play"         % "2.6.20",
      "uk.gov.hmrc"           %% "url-builder"  % "3.4.0-play-27"
    )
  )

  val test: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    shared = Seq("org.pegdown" % "pegdown" % "1.6.0" % Test),
    play25 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test),
    play26 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test),
    play27 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test)
  )

  val all: Seq[ModuleID] = compile ++ test
}
