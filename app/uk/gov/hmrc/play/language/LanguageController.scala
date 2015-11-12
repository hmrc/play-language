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

import play.api.i18n.Lang
import play.api.mvc.{Action, Call, Controller}
import play.api.Play.current
import LanguageUtils.{English, Welsh}

/**
  * LanguageController that switches the language of the current web application.
  *
  * This trait provides a means of switching the current language and redirecting the user
  * back to their original location. It expects a fallbackURL to be defined when implemented.
  *
  */
trait LanguageController extends Controller {

  /** A URL to fallback to if there is no referer found in the request header **/
  protected def fallbackURL: String

  /**
    * A function to switch the current language of the application.
    *
    * This function expects a Lang object as a parameter and will use this to switch
    * the current application language. This function expects a referer value within
    * the request header, and will redirect the user back to that value. If it is not
    * set then the redirect will be to the fallbackURL.
    *
    * The returned Redirect object will also contain a flashing parameter which can be
    * detected by controllers in order to show different behaviour if wanted.
    *
    * @param lang - The new language to switch to.
    * @return A Redirect to either the referer or fallbackURL, with the new language set.
    */
  protected def switchToLang(lang: Lang) = Action { implicit request =>
    request.headers.get(REFERER) match {
      case Some(ref) => Redirect(ref).withLang(lang).flashing(LanguageUtils.FlashWithSwitchIndicator)
      case None => Redirect(Call("GET", fallbackURL)).withLang(lang)
    }
  }
}

/**
  * Default implementation of the LanguageController.
  *
  * Adds support for switching the user between English and Welsh.
  */
object LanguageController extends LanguageController {

  override def fallbackURL = current.configuration.getString("language.fallbackUrl").getOrElse("/")

  def switchToEnglish = switchToLang(English)
  def switchToWelsh   = switchToLang(Welsh)
}