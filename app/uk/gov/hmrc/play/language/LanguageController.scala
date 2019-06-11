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

import javax.inject.Inject
import play.api.Application
import play.api.i18n.{I18nSupport, Lang, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.play.frontend.binders.RedirectUrlPolicy.Id
import uk.gov.hmrc.play.frontend.binders.{OnlyRelative, RedirectUrl, RedirectUrlPolicy}
import uk.gov.hmrc.play.frontend.binders.RedirectUrl._
import uk.gov.hmrc.play.frontend.controller.UnauthorisedAction

import scala.concurrent.Future

/**
  * LanguageController that switches the language of the current web application.
  *
  * This trait provides a means of switching the current language and redirecting the user
  * back to their original location. It expects a fallbackURL to be defined when implemented.
  * It also expects a languageMap to be defined, this provides a way of mapping strings to Lang objects.
  *
  */
abstract class LanguageController @Inject()(implicit val messagesApi: MessagesApi, application: Application) extends Controller with I18nSupport {

  /** A URL to fallback to if there is no referrer found in the request header **/
  protected def fallbackURL: String

  /** A map from a String to Lang object **/
  protected def languageMap: Map[String, Lang]

  /**
    * A public interface to switch to a new language.
    *
    * The language must be defined within the language map else the current language will be used.
    *
    * This function expects a language string as a parameter and will use this to switch
    * the current application language. This function expects a referrer value within
    * the request header, and will redirect the user back to that value. If it is not
    * set then the redirect will be to the fallbackURL.
    *
    * The returned Redirect object will also contain a flashing parameter which can be
    * detected by controllers in order to show different behaviour if wanted.
    *
    * @param language - The language string to switch to.
    * @return Redirect to referrer or fallbackURL, with new language. Or fallbackURL with default lang.
    */


  val policy: RedirectUrlPolicy[Id] = OnlyRelative

  def switchToLanguage(language: String): Action[AnyContent] = Action { implicit request =>
    val enabled = isWelshEnabled
    val lang =
      if (enabled) languageMap.getOrElse(language, LanguageUtils.getCurrentLang)
      else Lang("en")
    val redirectURL = request.headers.get(REFERER).map(RedirectUrl(_).get(policy).url).getOrElse(fallbackURL)

    Redirect(redirectURL).withLang(Lang.apply(lang.code)).flashing(LanguageUtils.FlashWithSwitchIndicator)
  }


  private def isWelshEnabled = {
    application.configuration.getBoolean("microservice.services.features.welsh-translation").getOrElse(true)
  }
}
