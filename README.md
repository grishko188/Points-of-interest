![Alt text](screenshots/playstore.jpg?raw=true "Bannert")

Points of interest
==================

**Gather and organize everything that matters**

This application allows users to easily receive and organize shared data, such as links, text, and images.
Create your points of interests easily with minimalistic interface:
- Use wizard to automatically parse link content
- Create your own custom categories
- Leave a message for yourself from future.
The app also allows for easy searching and filtering of stored information, making it easy to find the information you need quickly.

**Current version of application is offline only. It is a work in progress. So stay tuned for future updates.**

This app was created to showcase the latest technology in android development.
This application is implemented entirely using **Kotlin and Jetpack Compose.**

## APK / BUILD
APK - [Debug v1.0](app/debug/poi-debug.apk)
You can also clone project and build debug version. Note: Google Sign-In will not be fully functional in this case

## Screenshots

![Alt text](screenshots/screenshots.jpg?raw=true "Screen shots")

## Architecture
This application follows the classic SOLID based clean architecture approach. This approach differs from
[official architecture guidance](https://developer.android.com/topic/architecture), but it is actively used by many developers.
With this approach, the amount of boilerplate code increases, in favor of standardization and scalability.

## Modularization
The Points of interests application follows modularization strategy know as **"by layer"**.
*Note*: For larger projects i would recommend using **by layer and by feature** strategy.

For more details about modularization: [By layer or feature? Why not both?! Guide to Android app modularization](https://www.youtube.com/watch?v=16SwTvzDO0A)

## Features
In this project i tried to cover as many interesting learning cases as i could.
So this section will describe interesting features and use cases, as well as used technologies

### :domain

3rd party libraries:
 - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependencies injection
 - [Mockito](https://site.mockito.org/) for unit testing of UseCases

Features:
 - [Use cases](/domain/src/main/java/com/grishko188/domain/features/poi/interactor)
 - [Domain level models](/domain/src/main/java/com/grishko188/domain/features/poi/models)
 - [Repository interface](/domain/src/main/java/com/grishko188/domain/features/poi/repo)
 - [Unit tests](/domain/src/test)

### :data

 3rd party libraries and technologies used:
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependencies injection
  - [Mockito](https://site.mockito.org/) for unit testing of UseCases
  - [Kotlin flows](https://developer.android.com/kotlin/flow)
  - [Room](https://developer.android.com/training/data-storage/room) database
  - [Jetpack Datastore](https://developer.android.com/topic/libraries/architecture/datastore?gclid=Cj0KCQiA_bieBhDSARIsADU4zLd7nh0BnxbSv2Qso9alAQRfT6xbA4ct3zZGL4Ic5bgy03j84knDQKcaAmK6EALw_wcB&gclsrc=aw.ds)
  - [Protobuf](https://protobuf.dev/)
  - [Retrofit](https://square.github.io/retrofit/)
  - [OkHttp](https://square.github.io/okhttp/)
  - [Jsoup](https://jsoup.org/)

  Testing:
  - [Hilt testing](https://developer.android.com/training/dependency-injection/hilt-testing)
  - [Mockito](https://site.mockito.org/)
  - [Robolectric](https://robolectric.org/)

 Features:
  - [Database](/data/src/main/java/com/grishko188/data/database)
  - [DI Modules](/data/src/main/java/com/grishko188/data/di)
  - [Data repository](/data/src/main/java/com/grishko188/data/features/poi/repository)
  - [Data level models](/data/src/main/java/com/grishko188/data/features/poi/model)
  - [DAO](/data/src/main/java/com/grishko188/data/features/poi/db)
  - [Data sources](/data/src/main/java/com/grishko188/data/features/poi/datasource)
  - [Network calls](/data/src/main/java/com/grishko188/data/features/poi/api)
  - [Unit tests](/data/src/test)

 Learning cases:
  - [Room + Kotlin flows](/data/src/main/java/com/grishko188/data/features/poi/db/PoiDao.kt)
  - [Room and Dynamic ORDER BY](/data/src/main/java/com/grishko188/data/features/poi/db/PoiDao.kt)
  - Room and FTS (full test search): Look how to [define models](/data/src/main/java/com/grishko188/data/features/poi/model/PoiEntity.kt),
  [add entities to the data base](/data/src/main/java/com/grishko188/data/database/PoiDatabase.kt),
  [create search query function](/data/src/main/java/com/grishko188/data/features/poi/db/PoiDao.kt)
  and [perform the search](/data/src/main/java/com/grishko188/data/features/poi/datasource/PoiLocalDataSource.kt)
  - [How to parse HTML page with Jsoup](/data/src/main/java/com/grishko188/data/features/poi/datasource/WizardRemoteDataSource.kt)
  - [How to use Proto DataStore](https://developer.android.com/codelabs/android-proto-datastore#0):
  Look how to [setup gradle file: protobuf {} section](/data/build.gradle), [define proto models](/data/src/main/proto),
  [provide datastore using Hilt](/data/src/main/java/com/grishko188/data/di/DatastoreModule.kt),
  [create serializers](/data/src/main/java/com/grishko188/data/features/profile/datastore/Serializers.kt)
  and finally how to [use data store](/data/src/main/java/com/grishko188/data/features/profile/datasource/ProfileLocalDataSource.kt)
  - [How to test using Hilt and Robolectric](/data/src/test/java/com/grishko188/data/poi/datasource/PoiDataSourceInstrumentedTest.kt)
  - [How to test using Hilt and Robolectric and Mockito for maximum flexibility](/data/src/test/java/com/grishko188/data/poi/datasource/WizardSuggestionDataSourceInstrumentedTest.kt)
  - [How to test Room database](/data/src/test/java/com/grishko188/data/poi/db/PoiDaoInstrumentedTest.kt)
  using [Hilt's @TestInstallIn and Room's inMemoryDatabaseBuilder](/data-test/src/main/java/com/grishko188/data_test/TestDatabaseModule.kt)
  - [How to test Jetpack DataStore based datasource](/data/src/test/java/com/grishko188/data/profile/datasource/ProfileDataSourceInstrumentedTest.kt)
    using [Hilt's @TestInstallIn and TemporaryFolder](/data-test/src/main/java/com/grishko188/data_test/TestDataStoreModule.kt)
  - [How to test operations with files](/data/src/test/java/com/grishko188/data/poi/datasource/ImageDataSourceInstrumentedTest.kt)
  using [TemporaryFolder](/data-test/src/main/java/com/grishko188/data_test/TestCacheFolderModule.kt)

### :app (presentation layer)

3rd party libraries and technologies used:
  - [Compose](https://developer.android.com/jetpack/compose?gclid=CjwKCAiAoL6eBhA3EiwAXDom5uovlfrS1-2xp88b8zKsFzkiW36VKaFC01x9UM7zCvrIpCnRptZGJhoCq90QAvD_BwE&gclsrc=aw.ds)
  - [Android ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=CjwKCAiAoL6eBhA3EiwAXDom5oKABL8-HMrHV2XjQTCwKqtV-iMS4fTKJwgFsJDnzSwuNmDy0vEHyxoCqwkQAvD_BwE&gclsrc=aw.ds0)
  - [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
  - [Splash Screen Api](https://developer.android.com/develop/ui/views/launch/splash-screen)
  - [Coil for Compose](https://coil-kt.github.io/coil/compose/) image loading library
  - [Flow layout](https://google.github.io/accompanist/flowlayout/)
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependencies injection
  - [Work Manager](https://site.mockito.org/) for unit testing of UseCases
  - [Kotlin flows](https://developer.android.com/kotlin/flow)
  - [Google Sign In](https://developers.google.com/identity/sign-in/android/start-integrating)

Testing:
  - [Hilt testing](https://developer.android.com/training/dependency-injection/hilt-testing)
  - [Mockito](https://site.mockito.org/)
  - [Robolectric](https://robolectric.org/)
  - [Work Manager testing](https://developer.android.com/guide/background/testing/persistent/integration-testing)
  - [Compose testing](https://developer.android.com/jetpack/compose/testing)

Learning cases:

  **Work in progress**, please stay tuned for this section =)

## License
**Points of interest** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.