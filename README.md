[![codecov](https://codecov.io/gh/tfandkusu/try_graphql_android/branch/main/graph/badge.svg?token=BT4AN27WS3)](https://codecov.io/gh/tfandkusu/try_graphql_android)

# Try GraphQL Android

Try QraphQL Android client using [apollo-kotlin](https://github.com/apollographql/apollo-kotlin)

# Functionality

This app creates, updates and deletes GitHub Issues using [GitHub GraphQL API](https://docs.github.com/graphql).

| Issue list | Edit |
| --- | --- |
| <img src="https://user-images.githubusercontent.com/16898831/154813925-272424c4-4d81-4f6f-8b4f-483960838470.png" width="200"> | <img src="https://user-images.githubusercontent.com/16898831/154813927-16230a98-17db-4e9b-8f43-a9156b686366.png" width="200"> |

# How to build

Edit `local.properties`

## local.properties

| key | value |
| --- | --- |
| github_token | Your [Github personal access token](https://docs.github.com/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token). Scope is **repo** |
| owner_name | Repository's user name or organization name. |
| repository_name | Repository's name. |

## example

```local.properties
github_token=Your Github personal token
owner_name=tfandkusu
repository_name=try_graphql_android
```


# Template

This repository is generated from [tfandkusu/android_app_template](https://github.com/tfandkusu/android_app_template)

# Architecture

- **MVVM** of [Android recommended app architecture](https://developer.android.com/jetpack/guide#recommended-app-arch) 
- **Use Case** for resolving fat ViewModel problem

# Module structure

<img src="https://user-images.githubusercontent.com/16898831/147387105-669464f2-9e86-405a-b13e-7fd4213920bc.png" width="720">

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

## localDataStore

- Save cache time to local storage using Room.

## remoteDataStore

- API client using [apollo-kotlin](https://github.com/apollographql/apollo-kotlin)
- Save and watch cache using [normalized cache](https://www.apollographql.com/docs/kotlin/v2/essentials/normalized-cache/) of apollo-kotlin.

# Technology used

All libraries used are defined in [lib.versions.toml](https://github.com/tfandkusu/try_graphql_android/blob/main/gradle/libs.versions.toml)

**Ref:** [The version catalog TOML file format](https://docs.gradle.org/7.0.2/userguide/platforms.html#sub::toml-dependencies-format)

## View layer

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

## Presentation layer

- [androidx.compose.runtime:runtime-livedata](https://developer.android.com/jetpack/compose/libraries#streams)

## Data layer

- [apollo-kotlin](https://github.com/apollographql/apollo-kotlin)
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

## Other

- [OSS Licenses Gradle Plugin](https://github.com/google/play-services-plugins/tree/master/oss-licenses-plugin)
- [Timber](https://github.com/JakeWharton/timber)

# References

- [UnidirectionalViewModel](https://github.com/DroidKaigi/conference-app-2021/blob/main/uicomponent-compose/core/src/main/java/io/github/droidkaigi/feeder/core/UnidirectionalViewModel.kt) from [DroidKaigi/conference-app-2021](https://github.com/DroidKaigi/conference-app-2021)
