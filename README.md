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

## APK
[Debug v1.0](app/debug/poi-debug.apk)

## Screenshots

![Alt text](screenshots/screenshots.jpg?raw=true "Screen shots")

## Architecture
This application follows the classic SOLID based clean architecture approach. This approach differs from
[official architecture guidance](https://developer.android.com/topic/architecture), иut it is actively used by many developers.
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

Features
 - [Use cases](/domain/src/main/java/com/grishko188/domain/features/poi/interactor)
 - [Domain level models](/domain/src/main/java/com/grishko188/domain/features/poi/models)
 - [Repository interface](/domain/src/main/java/com/grishko188/domain/features/poi/repo)
 - [Unit tests](/domain/src/test)


## License
**Points of interest** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.