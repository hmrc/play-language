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

import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.{TimeZone, ULocale}
import org.joda.time.{DateTime, LocalDate}
import play.api.Play
import play.api.i18n.{Lang, Messages}
import play.api.mvc._

object LanguageUtils {

  val English = Lang("en", "GB")
  val Welsh = Lang("cy", "GB")

  val WelshLangCode = "cy-GB"
  val EnglishLangCode = "en-GB"

  def getCurrentLang(implicit request: RequestHeader): Lang = {
    Play.maybeApplication.map { implicit app =>
      val maybeLangFromCookie = request.cookies.get(Play.langCookieName).flatMap(c => Lang.get(c.value))
      maybeLangFromCookie.getOrElse(Lang.preferred(request.acceptLanguages))
    }.getOrElse(request.acceptLanguages.headOption.getOrElse(Lang.defaultLang))
  }

  object Dates {

    private val londonTimeZone = TimeZone.getTimeZone("Europe/London")
    private val welshLocale = new ULocale(WelshLangCode)
    private val englishLocale = new ULocale(EnglishLangCode)

    private def createDateFormatForPattern(pattern: String)(implicit lang: Lang) = {
      val locale = if (lang.code == WelshLangCode) welshLocale else englishLocale
      val sdf = new SimpleDateFormat(pattern, locale)
      sdf.setTimeZone(londonTimeZone)
      sdf
    }

    private def dateFormat(implicit lang: Lang) = createDateFormatForPattern("d MMMM y")
    private def dateFormatAbbrMonth(implicit lang: Lang) = createDateFormatForPattern("d MMM y")
    private def shortDateFormat(implicit lang: Lang) = createDateFormatForPattern("yyyy-MM-dd")
    private def easyReadingDateFormat(implicit lang: Lang) = createDateFormatForPattern("EEEE d MMMM yyyy")
    private def easyReadingTimestampFormat(implicit lang: Lang) = createDateFormatForPattern("h:mmaa")

    def formatDate(date: LocalDate)(implicit lang: Lang) = dateFormat.format(date.toDate)

    def formatDateAbbrMonth(date: LocalDate)(implicit lang: Lang) = dateFormatAbbrMonth.format(date.toDate)

    def formatDate(date: Option[LocalDate], default: String)(implicit lang: Lang) =
      date match {
        case Some(d) => dateFormat.format(d.toDate)
        case None => default
      }

    def formatEasyReadingTimestamp(date: Option[DateTime], default: String)(implicit lang: Lang) =
      date match {
        case Some(d) =>
          val time = easyReadingTimestampFormat.format(d.toDate).toLowerCase
          val date = easyReadingDateFormat.format(d.toDate)
          s"$time, $date"
        case None => default
      }

    def shortDate(date: LocalDate)(implicit lang: Lang) = shortDateFormat.format(date.toDate)

    def formatDays(numberOfDays: Int)(implicit lang: Lang) = {
      val dayOrDays = if(numberOfDays == 1) Messages("language.day.singular") else Messages("language.day.plural")
      s"$numberOfDays $dayOrDays"
    }

  }


}