# Play Language

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/hmrc/play-language.svg)](https://travis-ci.org/hmrc/play-language) [![Download](https://api.bintray.com/packages/hmrc/releases/play-language/images/download.svg)](https://bintray.com/hmrc/releases/play-language/_latestVersion)

Play library to provide common language support and switching functionality for Play projects.

## Endpoints

This library adds a new endpoint:

```
 /language/:lang     - Switches the current language to the lang, if defined in languageMap.
```

## Setup

Add the library to the project dependencies:

``` scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")
libraryDependencies += "uk.gov.hmrc" %% "play-language" % "[INSERT VERSION]"
```

## Configuration - Play 2.6 and 2.7 (version 4.7.0 and higher)

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


#### Using play-language's `language_selection.scala.html`:
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
