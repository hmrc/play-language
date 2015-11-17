#Play Language

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/hmrc/play-language.svg)](https://travis-ci.org/hmrc/play-language) [ ![Download](https://api.bintray.com/packages/hmrc/releases/play-language/images/download.svg) ](https://bintray.com/hmrc/releases/play-language/_latestVersion)

Play library to provide common language support and switching functionality for Play projects.

##Endpoints

This adds a new endpoint:

```
 /language/:lang     - Switches the current language to the lang, if defined in languageMap.
```

##Setup

Add the library to the project dependencies:

``` scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")
libraryDependencies += "uk.gov.hmrc" %% "play-language" % "[INSERT VERSION]"
```

Add the following to the routes file:

```
->     /                                    language.Routes
```

Additionally, add the following to the application conf file for each language you support:

```
application.langs="en,cy"
```

## Configuration

The plugin expects to find the fallback URL in the application config under the key `$env.language.fallbackUrl`. This is the URL that the plugin will redirect the user to if no referer value is found in the header, which shouldn't happen in a normal user journey.

```
Dev {
    language {
    	fallbackUrl = "https://localhost:$port/some-service-url/"
    }
}
Prod {
    language {
        fallbackUrl = some.service
    }
}
```

When you want to show a language switch to the user, use the language selection template.

```
@language_selection()					// No custom classes.
@language_selection(Some("custom-class"))	// Custom classes.
```

Add an implicit Lang object to each view you wish to support multiple languages.

``` scala
@()(implicit lang: Lang)
```

In order to show Welsh text to the user, create a `messages.cy` file within `/conf` and put your translations within there, using the same message keys.

## Extending

To provide support for more languages, create your own custom LanguageController:

``` scala
object CustomLanguageController extends LanguageController {
  override def fallbackURL = current.configuration.getString("language.fallbackUrl").getOrElse("/")
  override def languageMap = Map("english" -> Lang("en-GB"),
                                 "spanish" -> Lang("es"),
                                 "french"  -> Lang("fr"))
}
```

Then replace the above routes code with:

```
GET     /language/:lang       uk.gov.hmrc.project.CustomLanguageController.switchToLanguage(lang: String)
```

When you want to show a language switch to the user, use the language selection template and override the default language map.

``` scala
@language_selection(CustomLanguageController.languageMap)                   // No custom classes.
@language_selection(Some("custom-class"), CustomLanguageController.languageMap)   // Custom classes.
```

In order to show each additional text to the user, create a `messages.xx` file within `/conf`, where xx is the language code, and put your translations within there, using the same message keys.

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
