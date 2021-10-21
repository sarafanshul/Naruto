<h1 align="center">NarutoDex</h1>

<p align="center">
  <a href="https://devlibrary.withgoogle.com/products/android/repos/skydoves-pokedex"><img alt="Google" src="https://skydoves.github.io/badges/google-devlib.svg"/></a><br>
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/sarafanshul/Naruto/actions"><img alt="Build Status" src="https://github.com/skydoves/Pokedex/workflows/Android%20CI/badge.svg"/></a> 
</p>

<p align="center">  
NarutoDex is a demo application based on modern Android application tech-stacks and MVVM architecture.<br>This project is for focusing especially on the new library Hilt of implementing dependency injection.<br>
Also fetching data from the network and integrating persisted data in the database via repository pattern.<br>
  This uses <a href="https://github.com/sarafanshul/NarutoDB">NarutoDb</a> for beckend ops.
</p>
</br>

<p align="center">
<img src="/assets/mockups/naruto_mockup.png"/>
</p>

## Download
Go to the [Releases](https://github.com/sarafanshul/Naruto/releases) to download the latest APK.


<img src="/assets/mockups/app.gif" align="right" width="32%"/>

## Tech stack & Open-source libraries
- Minimum SDK level 29
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Hilt](https://dagger.dev/hilt/) for dependency injection.
- JetPack
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room Persistence - construct a database using the abstract layer.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - [Bindables](https://github.com/skydoves/bindables) - Android DataBinding kit for notifying data changes to UI layers.
  - Repository pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [Sandwich](https://github.com/skydoves/Sandwich) - construct lightweight http API response and handling error responses.
- [Moshi](https://github.com/square/moshi/) - A modern JSON library for Kotlin and Java.
- [Glide](https://github.com/bumptech/glide), [GlidePalette](https://github.com/florent37/GlidePalette) - loading images.
- [Bundler](https://github.com/skydoves/bundler) - Android Intent & Bundle extensions that insert and retrieve values elegantly.
- [Timber](https://github.com/JakeWharton/timber) - logging.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like ripple animation, cardView.

UI is utilising a dual-theme approach, allowing users to choose from light or dark theme. Components from Material Design library (MaterialCardview, MaterialToolbar, BottomNavigationView etc.) have been used due to their ability to switch between colour Primary and colour Surface. Detail Fragments are designed in a way that allows the user to drag and move contents - this behaviour is implemented by using MotionLayout. MaterialContainerTransform (shared element transitions), MaterialFadeThrough and MaterialElevationScale from Material Design library have been used to animate transitions between list and detail fragments.

## MAD Score
![summary](./assets/mad/summary.png)
![kotlin](./assets/mad/kotlin.png)

## Architecture
NarutoDex is based on MVVM architecture and a repository pattern.

![architecture](/assets/architecture/mvvm.png)

## API
NarutoDex using the [NarutoDB](https://github.com/sarafanshul/NarutoDB) for constructing RESTful API.<br>
NarutoDB provides a RESTful API interface to highly detailed objects built from thousands of lines of data related to Naruto.

## Authors
[Anshul](https://github.com/sarafanshul)

## Find this repository useful? :heart:
__[follow](https://github.com/sarafanshul)__ me for my next creations! 🤩

# License
```xml
Designed and developed by 2020 sarafanshul (Anshul Saraf)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
