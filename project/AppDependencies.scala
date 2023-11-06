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

  val shared = Seq(
    "com.ibm.icu"        % "icu4j" % "69.1"
  )

  val play28 = Seq(
    "com.typesafe.play"      %% "play"               % "2.8.20",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"   % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"       % "0.35.10" % Test
  )

  val play29 = Seq(
    "com.typesafe.play"      %% "play"               % "2.9.0",
    "org.scalatestplus.play" %% "scalatestplus-play" % "6.0.0"   % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"       % "0.64.8"  % Test
  )
}
