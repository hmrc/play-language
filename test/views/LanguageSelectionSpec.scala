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

package views

import org.scalatest._
import play.api.i18n.Messages
import play.api.test.FakeApplication
import play.api.test.Helpers._
import uk.gov.hmrc.play.language.LanguageUtils.{English, Welsh}

class LanguageSelectionSpec extends WordSpec with ShouldMatchers {

  "Language selection template view" should {

    "give a link to switch to Welsh when current language is English" in {
      val html = views.html.language_selection.render(English)
      contentType(html) should be ("text/html")
      contentAsString(html) should include (Messages("id=\"welsh-switch\""))
      contentAsString(html) should include ("/switch-to-welsh")
    }

    "show correct current language message when current language is English" in running(new FakeApplication) {
      val html = views.html.language_selection.render(English)
      contentType(html) should be ("text/html")
      contentAsString(html) should include (Messages("language.switch.english") + " |")
    }

    "give a link to switch to English when current language is Welsh" in {
      val html = views.html.language_selection.render(Welsh)
      contentType(html) should be ("text/html")
      contentAsString(html) should include (Messages("id=\"english-switch\""))
      contentAsString(html) should include ("/switch-to-english")
    }

    "show correct current language message when current language is Welsh" in running(new FakeApplication) {
      val html = views.html.language_selection.render(Welsh)
      contentType(html) should be ("text/html")
      contentAsString(html) should include ("| " + Messages("language.switch.welsh"))
    }

  }

}
