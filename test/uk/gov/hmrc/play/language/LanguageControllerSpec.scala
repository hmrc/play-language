/*
 * Copyright 2019 HM Revenue & Customs
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

import org.scalatestplus.play.PlaySpec
import play.api.Play
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Cookie
import play.api.test.Helpers._
import play.api.test._
import uk.gov.hmrc.play.frontend.binders.RedirectUrl
import uk.gov.hmrc.play.language.LanguageUtils._

class TestLanguageController extends LanguageController {

  override protected def fallbackURL = "http://gov.uk/fallback"

  override def languageMap = Map("english" -> English, "cymraeg" -> Welsh)
}

class LanguageControllerSpec extends PlaySpec with PlayRunners {

  private val refererValue = "/gov.uk"
  private val fallbackValue = "/fallback"

  "The switch language endpoint" should {

    "change to welsh when language is set to Welsh" in {
      running() {
        app =>
          val sut = app.injector.instanceOf[TestLanguageController]
          val res = sut.switchToLanguage(RedirectUrl("/dave"), "cymraeg")(FakeRequest())
          cookies(res).get(Play.langCookieName) match {
            case Some(c: Cookie) => c.value.mustBe(WelshLangCode)
            case _ => fail("PLAY_LANG cookie was not cy")
          }
      }
    }

    "not change to welsh with feature flag is set to false" in {
      val build = new GuiceApplicationBuilder().configure(Map("microservice.services.features.welsh-translation" -> false)).build()
      running(build) {
        val sut = build.injector.instanceOf[TestLanguageController]
        val res = sut.switchToLanguage(RedirectUrl("/fallback"),"cymraeg")(FakeRequest())
        cookies(res).get(Play.langCookieName) match {
          case Some(c: Cookie) => c.value must be(EnglishLangCode)
          case _ => fail("PLAY_LANG cookie was not found.")
        }
      }
    }

    "respond with a See Other (303) status when a referrer is in the header." in {
      running() { app =>
        val sut = app.injector.instanceOf[TestLanguageController]
        val request = FakeRequest().withHeaders(REFERER -> refererValue)
        val res = sut.switchToLanguage(RedirectUrl("/fallback"),"english")(request)
        status(res) must be(SEE_OTHER)
      }
    }

    "respond with a See Other (303) status when no referrer is in the header." in {
      running() { app =>
        val sut = app.injector.instanceOf[TestLanguageController]
        val res = sut.switchToLanguage(RedirectUrl("/fallback"),"english")(FakeRequest())
        status(res) must be(SEE_OTHER)
      }
    }

    "set the redirect location to the value of the referrer header." in {
      running() { app =>
        val sut = app.injector.instanceOf[TestLanguageController]
        val request = FakeRequest().withHeaders(REFERER -> refererValue)
        val res = sut.switchToLanguage(RedirectUrl("/gov.uk"),"english")(request)
        redirectLocation(res) must be(Some(refererValue))
      }
    }

    "set the redirect location to the fallback value when no referrer is in the header." in {
      running() { app =>
        val sut = app.injector.instanceOf[TestLanguageController]
        val res = sut.switchToLanguage(RedirectUrl("/fallback"),"english")(FakeRequest())
        redirectLocation(res) must be(Some(fallbackValue))
      }
    }

    "set the language in a cookie." in {
      running() { app =>
        val sut = app.injector.instanceOf[TestLanguageController]
        val res = sut.switchToLanguage(RedirectUrl("/fallback"), "english")(FakeRequest())
        cookies(res).get(Play.langCookieName) match {
          case Some(c: Cookie) => c.value must be(EnglishLangCode)
          case _ => fail("PLAY_LANG cookie was not found.")
        }
      }
    }
  }
}
