[![codecov](https://codecov.io/gh/tfandkusu/android_app_template/branch/main/graph/badge.svg?token=DQI5AN5H0Q)](https://codecov.io/gh/tfandkusu/android_app_template)

# Android app template

# Functionality

This repository is a template for Android app.
So it does not have any practical features.

It displays a list of [tfandkusu](https://github.com/tfandkusu)'s public GitHub repositories.

<img src="https://user-images.githubusercontent.com/16898831/146685977-85ab807c-bb04-4378-b005-71c7ecb9566c.png" width="200">

# Install

Current main branch.

[<img src="https://dply.me/tfafbv/button/large" alt="Try it on your device via DeployGate">](https://dply.me/tfafbv#install)

# Architecture

- **MVVM** of [Android recommended app architecture](https://developer.android.com/jetpack/guide#recommended-app-arch) 
- **Use Case** for resolving fat ViewModel problem

# Module structure

<img src="https://user-images.githubusercontent.com/16898831/147387105-669464f2-9e86-405a-b13e-7fd4213920bc.png" width="720">

Multiple `compose`, `presentation`, and  `usecase`  modules will be created for each feature.

## app

- Activity
- Compose navigation host

## compose

It has minimum dependency to speed up compose preview.

- Compose
- Compose preview
- ViewModel interface
- ViewModel implementation for compose preview

## presentation

- ViewModel implementation for production

## viewCommon

- Common API error handling
- Utility for ViewModel and LiveData

# Technology used

All libraries used are defined in [lib.versions.toml](https://github.com/tfandkusu/android_app_template/blob/main/gradle/libs.versions.toml)

**Ref:** [The version catalog TOML file format](https://docs.gradle.org/7.0.2/userguide/platforms.html#sub::toml-dependencies-format)

## View layer

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

## Presentation layer

- [androidx.compose.runtime:runtime-livedata](https://developer.android.com/jetpack/compose/libraries#streams)

## Data layer

- [Retrofit](https://github.com/square/retrofit)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)

## DI

- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Hilt Navigation Compose](https://developer.android.com/jetpack/compose/libraries#hilt-navigation)

## Unit test

- [MockK](https://github.com/mockk/mockk)
- [Kotest](https://github.com/kotest/kotest)
- [Robolectric](http://robolectric.org/)

## Coverage

- [Jacoco](https://www.eclemma.org/jacoco/)
- [Codecov](https://about.codecov.io/)

## CI/CD

- [GitHub Actions](https://docs.github.com/actions)
- [gradle-build-action](https://github.com/gradle/gradle-build-action)
- [Spotless plugin for Gradle](https://github.com/diffplug/spotless/tree/main/plugin-gradle)
- [Danger](https://danger.systems/ruby/)
- [danger-android_lint](https://github.com/loadsmart/danger-android_lint)  
- [Renovate](https://www.whitesourcesoftware.com/free-developer-tools/renovate/)
- [Firebase App Distribution](https://firebase.google.com/docs/app-distribution)
- [DeployGate](https://deploygate.com/)

## Other

- [OSS Licenses Gradle Plugin](https://github.com/google/play-services-plugins/tree/master/oss-licenses-plugin)

# References

- [UnidirectionalViewModel](https://github.com/DroidKaigi/conference-app-2021/blob/main/uicomponent-compose/core/src/main/java/io/github/droidkaigi/feeder/core/UnidirectionalViewModel.kt) from [DroidKaigi/conference-app-2021](https://github.com/DroidKaigi/conference-app-2021)
