# play-language

This library provides an abstract language switching controller, some utilities for
 writing Welsh dates and a (deprecated) language toggle component.

If you are using [play-frontend-hmrc](https://www.github.com/hmrc/play-frontend-hmrc) component
library, this library should not be used directly. Instead, please use the new
[hmrcLanguageSelectHelper](https://github.com/hmrc/play-frontend-hmrc#welsh-language-selection)
 component.

## LanguageUtils helpers ##
These helpers allow for the formatting of dates, in both English and Welsh.

There was an API change from version 4.x.x to 5.x.x, to remove the deprecated `joda.time` library from `play-language`.

From version 5.0.0 onwards, dates should be passed in as instances of `java.time.LocalDate`, not `joda.time.LocalDate`,
and dates with times should be passed in as instances of `java.time.LocalDateTime`, not `joda.time.DateTime`.

## Setup (for play-ui users only)

Add the library to the project dependencies:

``` scala
libraryDependencies += "uk.gov.hmrc" %% "play-language-play-xx" % "[INSERT VERSION]"
```

Where play-xx is your version of Play (e.g. play-30).

Ensure to add the resolvers to your `plugins.sbt`:

```scala
resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "[INSERT VERSION 3.0.0 OR HIGHER]")
```

## Configuration (for play-ui users only)

Create your own custom LanguageController

``` scala
import uk.gov.hmrc.YOUR_APPLICATION_PACKAGE.config.AppConfig
import com.google.inject.Inject
import javax.inject.Singleton
import play.api.i18n.Lang
import uk.gov.hmrc.play.language.{LanguageController, LanguageUtils}
import play.api.mvc._

class CustomLanguageController @Inject()(
                                        cc: ControllerComponents,
                                        languageUtils: LanguageUtils
                                      ) extends LanguageController(languageUtils, cc) {
  import appConfig._

  override protected def languageMap: Map[String, Lang] = {
    if (appConfig.welshLanguageSupportEnabled) Map(en -> Lang(en), cy -> Lang(cy))
    else Map(en -> Lang(en))
  }

  override def fallbackURL: String =
    "https://www.gov.uk/government/organisations/hm-revenue-customs"

}
```

The language map sets the display language for the name in the selection html element mapped to the language code to use.

Add the following to the application conf file for each language you support:

```
play.i18n.langs = ["en", "cy"]
```

Add the following to your application's custom routes file.

```
GET     /language/:lang       uk.gov.hmrc.project.controllers.CustomLanguageController.switchToLanguage(lang: String)
```

In order to show each language text to the user, create a `messages.xx` file within `/conf`, where xx is the language code, and put your translations within there, using the same message keys.


#### Using play-language's `language_selection.scala.html`: (for play-ui users only)
Add the following to your AppConfig trait.

``` scala
  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))

  def routeToSwitchLanguage = (lang: String) => routes.CustomLanguageController.switchToLanguage(lang)
```

In your main template:
``` scala
@views.html.language_selection(
            appConfig.languageMap,
            appConfig.routeToSwitchLanguage,
            Some("custom-class"))
```

If you wish to filter the languages displayed in your language selector to only display enabled languages, you can wrap you language Map in the LanguageUtils.onlyAvailableLanguages function.
Example, in your AppConfig class:

``` scala
import javax.inject.Inject
import uk.gov.hmrc.play.language.LanguageUtils

class AppConfig @Inject()(languageUtils: LanguageUtils) {
  def languageMap: Map[String, Lang] = languageUtils.onlyAvailableLanguages(
    Map(
      "english" -> Lang("en"),
      "cymraeg" -> Lang("cy")
    )
  )

  def routeToSwitchLanguage = (lang: String) => routes.CustomLanguageController.switchToLanguage(lang)
}

```

#### Using [govuk-template]("https://github.com/hmrc/govuk-template"):
Pass the following arguments to your template renderer
``` scala
"langSelector" -> {
  Map(
    "enUrl" -> controllers.routes.CustomLanguageController.switchToLanguage("english"),
    "cyUrl" -> controllers.routes.CustomLanguageController.switchToLanguage("cymraeg")
  )
},
"isWelsh" -> (messages.lang.code == "cy")

```

## License ##

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
