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
import play.api.mvc._
import play.api.Play.current
import uk.gov.hmrc.play.config.RunMode
import LanguageUtils.{English, Welsh}

/**
  * LanguageController that switches the language of the current web application.
  *
  * This trait provides a means of switching the current language and redirecting the user
  * back to their original location. It expects a fallbackURL to be defined when implemented.
  * It also expects a languageMap to be defined, this provides a way of mapping strings to Lang objects.
  *
  */
trait LanguageController extends Controller {

  /** A URL to fallback to if there is no referer found in the request header **/
  protected def fallbackURL: String

  /** A map from a String to Lang object **/
  protected def languageMap: Map[String, Lang]

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
  private def redirectWithLang(lang: Lang) = Action { implicit request =>
    request.headers.get(REFERER) match {
      case Some(ref) => Redirect(ref).withLang(lang).flashing(LanguageUtils.FlashWithSwitchIndicator)
      case None      => Redirect(fallbackURL).withLang(lang).flashing(LanguageUtils.FlashWithSwitchIndicator)
    }
  }

  /**
    * A public interface to switch to a new language.
    *
    * The language must be defined within the language map else no language will be set.
    *
    * @param language - The language string to switch to.
    * @return Redirect to referer or fallbackURL, with new language. Or fallbackURL with default lang.
    */
  def switchToLanguage(language: String): Action[AnyContent] = {
    languageMap.get(language) match {
      case Some(lang: Lang) => redirectWithLang(lang)
      case None             => Action {Redirect(fallbackURL)}
    }
  }
}

/**
  * Default implementation of the LanguageController.
  *
  * Adds support for switching the user between English and Welsh.
  */
object LanguageController extends LanguageController with RunMode {
  override def fallbackURL = current.configuration.getString(s"$env.language.fallbackUrl").getOrElse("/")
  override def languageMap = Map("english" -> English,
                                 "cymraeg" -> Welsh)
}