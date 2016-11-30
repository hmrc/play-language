/*
 * Copyright 2016 HM Revenue & Customs
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

import javax.inject.Inject

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Play
import play.api.i18n.Messages.Implicits._
import play.api.i18n.{Lang, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Cookie
import play.api.test.Helpers._
import play.api.test._
import uk.gov.hmrc.play.language.LanguageUtils._

class TestLanguageController @Inject()(val messagesApi: MessagesApi) extends LanguageController {

  private val fallbackValue = "http://gov.uk/fallback"

  override protected def fallbackURL: String = fallbackValue

  override def languageMap: Map[String, Lang] = Map("english" -> English,
    "cymraeg" -> Welsh)
}

class LanguageControllerSpec extends PlaySpec with OneAppPerSuite with PlayRunners with ScalaFutures with DefaultAwaitTimeout with IntegrationPatience {

  private val refererValue = "http://gov.uk"
  private val fallbackValue = "http://gov.uk/fallback"

  val builder = new GuiceApplicationBuilder()
    .overrides(bind[LanguageController].to[TestLanguageController])
    .build

  private val mockLanguageController: LanguageController = builder.injector.instanceOf[LanguageController]

  "The switch language endpoint" should {

    "respond with a See Other (303) status when a referer is in the header." in {
      val request = FakeRequest().withHeaders(REFERER -> refererValue)
      val res = mockLanguageController.switchToLanguage("english")(request)
      status(res) must be(SEE_OTHER)
    }

    "respond with a See Other (303) status when no referer is in the header." in {
      val res = mockLanguageController.switchToLanguage("english")(FakeRequest())
      status(res) must be(SEE_OTHER)
    }

    "set the redirect location to the value of the referer header." in {
      val request = FakeRequest().withHeaders(REFERER -> refererValue)
      val res = mockLanguageController.switchToLanguage("english")(request)
      redirectLocation(res) must be(Some(refererValue))
    }

    "set the redirect location to the fallback value when no referer is in the header." in {
      val res = mockLanguageController.switchToLanguage("english")(FakeRequest())
      redirectLocation(res) must be(Some(fallbackValue))
    }

    "should set the language in a cookie." in {
      val res = mockLanguageController.switchToLanguage("english")(FakeRequest())
      cookies(res).get(Play.langCookieName) match {
        case Some(c: Cookie) => c.value must be(EnglishLangCode)
        case _ => fail("PLAY_LANG cookie was not found.")
      }
    }
  }
}
