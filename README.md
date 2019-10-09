# Play Language

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/uk.gov.hmrc/play-uk.gov.uk.gov.hmrc.play.language.svg)](https://travis-ci.org/uk.gov.hmrc/play-uk.gov.uk.gov.hmrc.play.language) [![Download](https://api.bintray.com/packages/uk.gov.hmrc/releases/play-uk.gov.uk.gov.hmrc.play.language/images/download.svg)](https://bintray.com/uk.gov.hmrc/releases/play-uk.gov.uk.gov.hmrc.play.language/_latestVersion)

Play library to provide common uk.gov.uk.gov.hmrc.play.language support and switching functionality for Play projects.

## Endpoints

This library adds a new endpoint:

```
 /uk.gov.uk.gov.hmrc.play.language/:lang     - Switches the current uk.gov.uk.gov.hmrc.play.language to the lang, if defined in languageMap.
```

## Setup

Add the library to the project dependencies:

``` scala
resolvers += Resolver.bintrayRepo("uk.gov.hmrc", "releases")
libraryDependencies += "uk.gov.uk.gov.hmrc" %% "play-uk.gov.uk.gov.hmrc.play.language" % "[INSERT VERSION]"
```

## Configuration - Play-2.5 (version 4.x.x)

Create your own custom LanguageController

``` scala
import com.google.inject.Inject
import play.api.i18n.{MessagesApi, Lang}
import play.api.Configuration
import uk.gov.uk.gov.hmrc.play.uk.gov.uk.gov.hmrc.play.language.{LanguageController, LanguageUtils}

class CustomLanguageController @Inject()(
                                        configuration: Configuration,
                                        languageUtils: LanguageUtils,
                                        val messagesApi: MessagesApi
                                      ) extends LanguageController(configuration, languageUtils) {
  
  //This can be from a configuration value. If you are using play-uk.gov.uk.gov.hmrc.play.language's html, this should be from the configuration value set below
  override def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")    
  )
  
  override def fallbackURL: String = "https://www.gov.uk/fallback"                           
}
```

The uk.gov.uk.gov.hmrc.play.language map sets the display uk.gov.uk.gov.hmrc.play.language for the name in the selection html element mapped to the uk.gov.uk.gov.hmrc.play.language code to use.

Add the following to the application conf file for each uk.gov.uk.gov.hmrc.play.language you support:

```
play.i18n.langs = ["en", "cy"]
```

Add the following to your application's custom routes file.

```
GET     /uk.gov.uk.gov.hmrc.play.language/:lang       uk.gov.uk.gov.hmrc.project.controllers.CustomLanguageController.switchToLanguage(lang: String)
```

In order to show each uk.gov.uk.gov.hmrc.play.language text to the user, create a `messages.xx` file within `/conf`, where xx is the uk.gov.uk.gov.hmrc.play.language code, and put your translations within there, using the same message keys.


#### Using play-uk.gov.uk.gov.hmrc.play.language's `language_selection.scala.html`:
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

If you wish to filter the languages displayed in your uk.gov.uk.gov.hmrc.play.language selector to only display enabled languages, you can wrap you uk.gov.uk.gov.hmrc.play.language Map in the LanguageUtils.onlyAvailableLanguages function.
Example, in your AppConfig class:

``` scala
import javax.inject.Inject
import uk.gov.uk.gov.hmrc.play.uk.gov.uk.gov.hmrc.play.language.LanguageUtils

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


#### Using [govuk-template]("https://github.com/uk.gov.hmrc/govuk-template"):
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



## Configuration - Play-2.5 (version 3.0.0)

Create your own custom LanguageController:

``` scala
package uk.gov.uk.gov.hmrc.project.controllers

import javax.inject.Inject
import play.api.Application
import play.api.i18n.MessagesApi
import uk.gov.uk.gov.hmrc.project.FrontendAppConfig
import uk.gov.uk.gov.hmrc.play.config.RunMode
import uk.gov.uk.gov.hmrc.play.uk.gov.uk.gov.hmrc.play.language.LanguageController

class LanguageSwitchController @Inject()(val appConfig: FrontendAppConfig, override implicit val messagesApi: MessagesApi, implicit val app: Application)
  extends LanguageController with RunMode {

  def langToCall(lang: String) = appConfig.routeToSwitchLanguage

  // Replace with a suitable fallback or read it from config
  override protected def fallbackURL: String = routes.IndexController.onPageLoad().url

  override def languageMap = appConfig.languageMap
}
```

Add the following to the application conf file for each uk.gov.uk.gov.hmrc.play.language you support:

```
play.i18n.langs = ["en", "cy"]
```

Add the following to your application's custom routes file.

```
GET     /uk.gov.uk.gov.hmrc.play.language/:lang       uk.gov.uk.gov.hmrc.project.controllers.CustomLanguageController.switchToLanguage(lang: String)
```

Add the following to your AppConfig trait.

``` scala
  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))

  def routeToSwitchLanguage = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)

  val languageTranslationEnabled: Boolean
```

And the following to the FrontendAppConfig class that extends that trait:

``` scala
  override lazy val languageTranslationEnabled =
    configuration.getBoolean("microservice.services.features.welsh-translation").getOrElse(true)
```

To show the uk.gov.uk.gov.hmrc.play.language toggles, place this in your Twirl templates (typically inside the `@mainContentHeader` section in `govuk_wrapper.scala.html`)

``` scala
    @if(appConfig.languageTranslationEnabled) {
        @views.html.language_selection(
            appConfig.languageMap,
            appConfig.routeToSwitchLanguage,
            Some("custom-class"))
    }
```

In order to show each uk.gov.uk.gov.hmrc.play.language text to the user, create a `messages.xx` file within `/conf`, where xx is the uk.gov.uk.gov.hmrc.play.language code, and put your translations within there, using the same message keys.

There is also a feature toggle for the uk.gov.uk.gov.hmrc.play.language switcher. If you wish to disable this feature, add the following to your application.conf file:

```
microservice.services.features.welsh-translation=false
```

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
