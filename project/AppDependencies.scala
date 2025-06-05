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

  val play29 = play("play-29")
  val play30 = play("play-30")

  private def play(playSuffix: String) = Seq(
    "com.ibm.icu"            %  "icu4j"              % "69.1",
    playOrg(playSuffix)      %% "play"               % playVersion(playSuffix),
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion(playSuffix) % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"       % "0.64.8"                             % Test
  )

  private def playVersion(playSuffix: String) =
    playSuffix match {
      case "play-29" => "2.9.7"
      case "play-30" => "3.0.7"
    }

  private def playOrg(playSuffix: String): String =
    playSuffix match {
      case "play-29" => "com.typesafe.play"
      case "play-30" => "org.playframework"
    }

  private def scalaTestPlusPlayVersion(playSuffix: String): String =
    playSuffix match {
      case "play-29" => "6.0.1"
      case "play-30" => "7.0.1"
    }
}
