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

## Configuration

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

In order to show each language text to the user, create a `messages.xx` file within `/conf`, where xx is the language code, and put your translations within there, using the same message keys.


## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
