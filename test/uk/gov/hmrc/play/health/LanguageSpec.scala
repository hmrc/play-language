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

package uk.gov.hmrc.play.language

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{WordSpec, ShouldMatchers}
import play.api.mvc.Call
import play.api.libs.ws.{WS}
import play.mvc.Results.Status
import uk.gov.hmrc.play.audit.http.HeaderCarrier
import play.api.test._
import play.api.test.Helpers._


class LanguageSpec extends WordSpec with ShouldMatchers with PlayRunners with ScalaFutures with DefaultAwaitTimeout with IntegrationPatience {

  trait Resource {
    this: WithServer =>
    def resource(path: String) = WS.url(s"http://localhost:$port" + path).get().futureValue
  }

  val testConfig = Map("application.router" -> "language.Routes")

  abstract class ServerWithConfig(conf: Map[String, String] = Map.empty) extends
    WithServer(FakeApplication(additionalConfiguration = testConfig ++ conf)) with Resource

  "The switch to English endpoint" should {
    "respond with a redirect status code when accessed with a valid referer header" in new ServerWithConfig() {
      val request = FakeRequest("GET", "/switch-to-english").withHeaders(REFERER -> "www.gov.uk")
      val Some(result) = route(request)
      status(result) should be (SEE_OTHER)
    }

    "respond with a redirect status code when accessed with no referer header" in new ServerWithConfig(){
      val request = FakeRequest("GET", "/switch-to-english")
      val Some(result) = route(request)
      status(result) should be (SEE_OTHER)
    }

  }

  "The switch to Welsh endpoint" should {

    "respond with a redirect status code when accessed with a valid referer header" in new ServerWithConfig() {
      val request = FakeRequest("GET", "/switch-to-welsh").withHeaders(REFERER -> "www.gov.uk")
      val Some(result) = route(request)
      status(result) should be (SEE_OTHER)
    }

    "respond with a redirect status code when accessed with no referer header" in new ServerWithConfig() {
      val request = FakeRequest("GET", "/switch-to-welsh")
      val Some(result) = route(request)
      status(result) should be (SEE_OTHER)
    }

  }
}