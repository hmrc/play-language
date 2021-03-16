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
import play.core.PlayVersion

object AppDependencies {

  val compile: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    shared = Seq(
      "com.ibm.icu"           %  "icu4j"        % "68.2",
      "com.typesafe.play"     %% "play"         % PlayVersion.current
    ),
    play26 = Seq(
      "uk.gov.hmrc"           %% "url-builder"  % "3.5.0-play-26"
    ),
    play27 = Seq(
      "uk.gov.hmrc"           %% "url-builder"  % "3.5.0-play-27"
    ),
    play28 = Seq(
      "uk.gov.hmrc"           %% "url-builder"  % "3.5.0-play-28"
    )
  )

  val test: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    shared = Seq("org.pegdown"            % "pegdown"             % "1.6.0" % Test),
    play26 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test),
    play27 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test),
    play28 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test)
  )

  val all: Seq[ModuleID] = compile ++ test
}
