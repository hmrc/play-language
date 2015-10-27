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

import org.joda.time.{DateTime, LocalDate}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.play.OneAppPerSuite
import play.api.test.FakeApplication
import play.api.test.Helpers._
import uk.gov.hmrc.play.language.LanguageUtils._
import uk.gov.hmrc.play.language.LanguageUtils.Dates._

class LanguageUtilsSpec extends FlatSpec with Matchers with OneAppPerSuite {

  val date = new LocalDate(2015,1,25)
  val dateAndTime = new DateTime(2015, 1, 25, 3, 45)

  "Method formatDate(date: LocalDate)" should "return correctly formatted date in both English and Welsh" in {
    formatDate(date)(English) shouldBe "25 January 2015"
    formatDate(date)(Welsh) shouldBe "25 Ionawr 2015"
  }

  "Method formatDate(date: Option[LocalDate], default: String)" should "return correctly formatted date in both English and Welsh" in {
    formatDate(Some(date),"n/a")(English) shouldBe "25 January 2015"
    formatDate(Some(date),"n/a")(Welsh) shouldBe "25 Ionawr 2015"
  }

  "Method formatDateAbbrMonth" should "return correctly formatted date in both English and Welsh" in {
    formatDateAbbrMonth(date)(English) shouldBe "25 Jan 2015"
    formatDateAbbrMonth(date)(Welsh) shouldBe "25 Ion 2015"
  }

  "Method shortDate" should "return correctly formatted date in both English and Welsh" in {
    shortDate(date)(English) shouldBe "2015-01-25"
    shortDate(date)(Welsh) shouldBe "2015-01-25"
  }

  "Method formatDate(date: Option[LocalDate], default: String)" should "return a default if None was passed as date" in {
    formatDate(None,"some_default") shouldBe "some_default"
  }

  "Method formatEasyReadingTimeStamp" should "return correctly formatted date and time in both English and Welsh" in {
    formatEasyReadingTimestamp(Some(dateAndTime), "default value")(English) shouldBe "3:45am, Sunday 25 January 2015"
    formatEasyReadingTimestamp(Some(dateAndTime), "default value")(Welsh) shouldBe "3:45am, Dydd Sul 25 Ionawr 2015"
  }

  "Method formatEasyReadingTimeStamp" should "return a default value if None was passed as dateTime" in {
    formatEasyReadingTimestamp(None, "some_default") shouldBe "some_default"
  }

  "Method formatDateRange" should "return correctly formatted date and time range in both English and Welsh" in {
    formatDateRange(date, date)(English) shouldBe "25 January 2015 to 25 January 2015"
    formatDateRange(date, date)(Welsh) shouldBe "25 Ionawr 2015 i 25 Ionawr 2015"
  }

  "Method formatDays" should "return correct singular/plural for day/days in both English and Welsh" in {
    formatDays(1)(English) shouldBe "1 day"
    formatDays(5)(English) shouldBe "5 days"
    formatDays(-15)(English) shouldBe "-15 days"

    formatDays(1)(Welsh) shouldBe "1 diwrnod"
    formatDays(5)(Welsh) shouldBe "5 diwrnod"
    formatDays(-15)(Welsh) shouldBe "-15 diwrnod"
  }

}
