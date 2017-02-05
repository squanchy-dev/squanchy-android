# Setting up Squanchy

To be able to run Squanchy, you'll need to set it up first. The setup process consists in setting a few properties and obtain a bunch of API keys. You don't need to touch anything in the application code, all the configuration is held in the `team-props` folder.

## Checklist

These are all the properties and API keys you'll need to set up Squanchy correctly:

 * A package name (e.g., `com.myconference`)
 * A query for the social feed, which is used to populate the Twitter feed (e.g., `#AndroidDev`)
 * A signing keystore with the associated password, alias and alias password
 * A base URL to use to compose the address of the RESTful endpoints exposed by the backend
 * A Google Maps API key (you can obtain it [here](https://developers.google.com/maps/documentation/android-api/signup))
 * A Fabric API key (you'll obtain it by adding the app to Fabric through the [Android Studio plugin](https://fabric.io/downloads/android-studio))
 * A Twitter API key and secret, which you can get by adding the Twitter kit to Fabric (you can add it through the [Android Studio plugin](https://fabric.io/downloads/android-studio))

## How to set the properties up

Squanchy uses Novoda's [Gradle Build Properties plugin](https://github.com/novoda/gradle-build-properties-plugin) to make the configuration effortless. In a nutshell, all those settings are specified through three files in the `team-props` folder:
 * `application.properties`
 * `secrets.properties`
 * `releaseSigningConfig.properties`

In that folder you'll find a `.sample` version for each of those files. Make a copy of each, taking out the `.sample` extension (e.g., copy `secrets.properties.sample` renaming it as `secrets.properties`).

⚠ **NOTE:** those `.properties` files are ignored by git by default because they're **not** meant to be saved into version control!

### Application properties
This file contains the application package (`applicationId`), and the social query (`socialQuery`) to use for the Twitter timeline in the app.

### Release signing configuration
This file will tell Gradle which keystore to use (`storeFile`) and its password (`storePassword`), in order to correctly sign release apks. In addition to that, it contains the name of the specific key alias (`keyAlias`) and the password (`keyPassword`) to use.

⚠ **NOTE:** make sure you don't commit the keystore or its passwords! It's sensible data, meant to be private.

### App secrets
This file contains a bunch of private configuration details that are not needed for signing an app, but are needed to make it work.

 * `fabricApiKey` is the API key to use for Fabric (and thus, Crashlytics). To obtain this, enable the app for Fabric from the [Fabric plugin](https://fabric.io/downloads/android-studio), let it change stuff, get the API key it generates, and put it into the properties file. Then revert whatever changes the Fabric wizard might have applied to the code
 * `googleMapsApiKey` is the API key for [Google Maps](https://developers.google.com/maps/documentation/android-api/signup). This is used for the venue map and directions
 * `twitterApiKey` and `twitterSecret` are used by the Twitter SDK. You can obtain them by enabling the Twitter Kit in Fabric; just click the corresponding button in the Fabric plugin UI in Android Studio, grab the keys from wherever it adds them, move them to the properties file, and revert whatever other changes the wizard might have done to the code
 * `baseUrl` is the URL to use as prefix when composing the HTTP requests to the backend

### Google Play Store keys

There is also a `play-store-keys.json` file that you can generate from your Developer Account in the Play Store. In order to do so, please follow
the instructions [here](https://github.com/Triple-T/gradle-play-publisher#google-play-service-account).
Please note that when you change a property on the settings, you will have to download the new JSON file.
