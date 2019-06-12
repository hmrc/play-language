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

import play.core.PlayVersion
import sbt._


object AppDependencies {

  private val urlBuilderVersion = "3.1.0"
  private val ibm4jVersion = "63.1"
  private val bootstrapPlay25Version = "4.13.0"

  def compile(): Seq[ModuleID] = Seq(
    "com.typesafe.play" %% "play" % PlayVersion.current % "provided",
    "uk.gov.hmrc" %% "url-builder" % urlBuilderVersion,
    "com.ibm.icu" % "icu4j" % ibm4jVersion,
    "uk.gov.hmrc" %% "bootstrap-play-25" % bootstrapPlay25Version
  )

  private val scalaTestPlusVersion = "2.0.0"
  private val pegdownVersion = "1.6.0"
  private val scope = "test"

  def test(): Seq[ModuleID] = Seq(
    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusVersion % scope,
    "org.pegdown" % "pegdown" % pegdownVersion % scope
  )
  
}
