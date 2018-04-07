# Setting up Squanchy

To be able to run Squanchy, you'll need to set it up first. The setup process consists in setting a few properties and obtain a bunch of API keys. You
don't need to touch anything in the application code, all the configuration is held in the `team-props` folder.

## Checklist
These are all the properties and API keys you'll need to set up Squanchy correctly:

 * A package name (e.g., `com.myconference`)
 * A query for the social feed, which is used to populate the Twitter feed (e.g., `#AndroidDev`)
 * A signing keystore with the associated password, alias and alias password
 * A base URL to use to compose the address of the RESTful endpoints exposed by the backend
 * A Fabric API key (you'll obtain it by adding the app to Fabric through the [Android Studio plugin](https://fabric.io/downloads/android-studio))
 * An Algolia project, used to perform search in the app, which you can create on the [Algolia website](https://www.algolia.com)
 * A `google-services.json` file that you can obtain from the [Firebase Console](https://console.firebase.google.com/) for your project

## How to set the properties up
Squanchy uses Novoda's [Gradle Build Properties plugin](https://github.com/novoda/gradle-build-properties-plugin) to make the configuration
effortless. In a nutshell, all those settings are specified through three files in the [`team-props`
scaffolding](https://github.com/novoda/novoda/tree/master/scaffolding) folder:

 * `application.properties`
 * `secrets.properties`
 * `debugSigningConfig.properties`
 * `releaseSigningConfig.properties`

With the exception of `debugSigningConfig.properties`, all those files are ignored from Git because they contain secrets that must **never** be
disclosed or added to the repository. In that folder you'll find a `.sample` version for each of these files. Make a copy of each, taking out the
`.sample` extension (e.g., copy `secrets.properties.sample` renaming it as `secrets.properties`).

### Application properties
This file contains the application package (`applicationId`) and the Algolia id (`algoliaId`)

### Release signing configuration
This file will tell Gradle which keystore to use (`storeFile`) and its password (`storePassword`), in order to correctly sign release apks. In additio
n to that, it contains the name of the specific key alias (`keyAlias`) and the password (`keyPassword`) to use.

⚠ **NOTE:** make sure you don't commit the keystore or its passwords! It's sensible data that must not leak into the repository.

### App secrets
This file contains a bunch of private configuration details that are not needed for signing an app, but are needed to make it work.

 * `fabricApiKey` is the API key to use for Fabric (and thus, Crashlytics). To obtain this, enable the app for Fabric from the
   [Fabric plugin](https://fabric.io/downloads/android-studio), let it change stuff, get the API key it generates, and put it into the properties
   file. Then revert whatever changes the Fabric wizard might have applied to the code

 * `algoliaApiKey` is the API key to use for Algolia, the framework responsible for the search feature in the app. After creating a project in the 
    [Algolia website](https://www.algolia.com), you can find it in the "API keys" section of the dashboard

### Google Play Store keys
There is also a `play-store-keys.json` file that you can generate from your Developer Account in the Play Store. In order to do so, please follow
the instructions [here](https://github.com/Triple-T/gradle-play-publisher#google-play-service-account).

Please note that when you change something in the Play Store console, you will have to download the new JSON file.  

You'll find a `play-store-keys.json.sample` in the `team-props` folder too, for reference.

## The Firebase/Play Services configuration JSON file
Once you have obtained the Firebase/Play Services configuration JSON file from the [Firebase Console](https://console.firebase.google.com/) for your
project, simply follow the [official instructions](https://developers.google.com/android/guides/google-services-plugin#adding_the_json_file) and copy
it in the `app` folder. Git will ignore it by default since it contains sensible data.
