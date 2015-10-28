#Play Language Plugin

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/hmrc/play-language.svg)](https://travis-ci.org/hmrc/play-language-support) [ ![Download](https://api.bintray.com/packages/hmrc/releases/play-language-support/images/download.svg) ](https://bintray.com/hmrc/releases/play-language/_latestVersion)

Plugin to provide common language support and switching functionality for Play +2.2.3 projects.

##Endpoints

This adds two endpoints:

```
     /switch-to-english     - Switches the current language to English
	 /switch-to-welsh 		- SWitches the current language to Welsh
```

##Setup

Add the jar to the projects dependencies:

```
    "uk.gov.hmrc" %% "play-language" % "[INSERT VERSION]"
```

Add plugin to play.plugins:

```
    {priority}:uk.gov.hmrc.play.language.LanguagePlugin
```

Add the following to the routes file:

```
    ->     /                                    language.Routes
```

Additionally, add the following to the application conf file:

```
    application.langs="en,cy"
```

## Configuration

The plugin expects to find the fallback URL in an `application.conf` under the key `language.fallbackUrl`. This is the URL that the plugin will redirect the user to if no referer value is found in the header, which shouldn't happen in a normal user journey.

```
    language {
    	fallbackUrl = "https://localhost:$port/some-service-url/"
    }
```

When you need to show a language switch to the user, use the language selection template.

```
    @language_selection(Some("custom-class"))
```

In order to show Welsh text to the user, create a `messages.cy` file within `/conf` and put your translations within there, using the same message keys.

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
