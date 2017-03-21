#Play Language

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/hmrc/play-language.svg)](https://travis-ci.org/hmrc/play-language) [ ![Download](https://api.bintray.com/packages/hmrc/releases/play-language/images/download.svg) ](https://bintray.com/hmrc/releases/play-language/_latestVersion)

Play library to provide common language support and switching functionality for Play projects.

##Endpoints

This library adds a new endpoint:

```
 /language/:lang     - Switches the current language to the lang, if defined in languageMap.
```

##Setup

Add the library to the project dependencies:

``` scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")
libraryDependencies += "uk.gov.hmrc" %% "play-language" % "[INSERT VERSION]"
```

## Configuration - Play-2.3 (version 2.2.0)

Create your own custom LanguageController:

``` scala
object CustomLanguageController extends LanguageController with RunMode {
  
  /** Converts a string to a URL, using the route to this controller. **/
  def langToCall(lang: String): Call = controllers.routes.CustomLanguageController.switchToLanguage(lang)

  /** Provides a fallback URL if there is no referer in the request header. **/
  override protected def fallbackURL: String = current.configuration.getString(s"$env.language.fallbackUrl").getOrElse("/")

  /** Returns a mapping between strings and the corresponding Lang object. **/
  override def languageMap: Map[String, Lang] = Map("english" -> Lang("en"),
                                                    "cymraeg" -> Lang("cy-GB"))
}
```

Add the following to the application conf file for each language you support:

```
application.langs="en,cy"
```

Add the following to your application's custom routes file. 

```
GET     /language/:lang       uk.gov.hmrc.project.CustomLanguageController.switchToLanguage(lang: String)
```

When you want to show a language switch to the user, use the language selection template.

``` scala
@language_selection(CustomLanguageController.languageMap, CustomLanguageController.langToCall, Some("custom-class"))
```

Add an implicit Lang object to each view you wish to support multiple languages.

``` scala
@()(implicit lang: Lang)
```

## Configuration - Play-2.5 (version 3.0.0)

Create your own custom LanguageController:

``` scala
package uk.gov.hmrc.project.controllers

import javax.inject.Inject

import play.api.Play
import play.api.i18n.{Lang, MessagesApi}
import play.api.mvc.Call
import uk.gov.hmrc.play.config.RunMode
import uk.gov.hmrc.play.language.LanguageController

class CustomLanguageController @Inject()(implicit val messagesApi: MessagesApi) extends LanguageController with RunMode {

  /** Converts a string to a URL, using the route to this controller. **/
  def langToCall(lang: String): Call = uk.gov.hmrc.exampleplay25.controllers.routes.CustomLanguageController.switchToLanguage(lang)

  /** Provides a fallback URL if there is no referer in the request header. **/
  override protected def fallbackURL: String = Play.current.configuration.getString(s"$env.language.fallbackUrl").getOrElse("/")

  /** Returns a mapping between strings and the corresponding Lang object. **/
  override def languageMap: Map[String, Lang] = Map("english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))
}
```

Add the following to the application conf file for each language you support:

```
application.langs="en,cy"
```

Add the following to your application's custom routes file.

```
GET     /language/:lang       @uk.gov.hmrc.project.controllers.CustomLanguageController.switchToLanguage(lang: String)
```

When you want to show a language switch to the user, use the language selection template.

``` scala
@import uk.gov.hmrc.project.controllers.CustomLanguageController
@import play.api.Application

@()(implicit request: Request[_], messages: Messages, application: Application)

@clc = @{ Application.instanceCache[CustomLanguageController].apply(application) }

 @uk.gov.hmrc.project.views.html.main_template(title = "Hello from example-play-25-frontend", bodyClasses = None) {
    <h1 id="message">Hello World!</h1>
    <h2>@Messages("some.message")</h2>

    @language_selection(clc.languageMap, clc.langToCall, Some("custom-class"))
 }
```

In order to show each language text to the user, create a `messages.xx` file within `/conf`, where xx is the language code, and put your translations within there, using the same message keys.

There is also a feature toggle for the language switcher. If you wish to disable this feature, add the following to your application.conf file:

```
microservice.services.features.welsh-translation=false
```

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
