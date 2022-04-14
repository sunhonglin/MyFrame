apply("$rootDir/gradle/configure-android-defaults.kts")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("dagger.hilt.android.plugin")
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    api(Dependencies.AndroidX.appcompat)
    api(Dependencies.AndroidX.activity)
    api(Dependencies.AndroidX.fragment)
    api(Dependencies.AndroidX.core)
    api(Dependencies.AndroidX.loader)
    api(Dependencies.AndroidX.customView)
    api(Dependencies.AndroidX.viewPager2)
    api(Dependencies.AndroidX.drawerLayout)
    api(Dependencies.AndroidX.constraintLayout)
    api(Dependencies.AndroidX.Annotation.runtime)

    // lifecycle
    api(Dependencies.AndroidX.Lifecycle.viewModel)
    api(Dependencies.AndroidX.Lifecycle.liveData)
    api(Dependencies.AndroidX.Lifecycle.runtime)
    api(Dependencies.AndroidX.Lifecycle.viewModelSavedState)
    kapt(Dependencies.AndroidX.Lifecycle.compiler)

    // room
    api(Dependencies.AndroidX.Room.ktx)
    kapt(Dependencies.AndroidX.Room.compiler)

    // hilt
    api(Dependencies.Google.Hilt.hilt)
    kapt(Dependencies.Google.Hilt.compiler)
    kapt(Dependencies.AndroidX.Hilt.compiler)

    // retrofit2 + okHttp3
    api(Dependencies.SquareUp.OkHttp3.runtime)
    api(Dependencies.SquareUp.OkHttp3.loggingInterceptor)
    api(Dependencies.SquareUp.Retrofit2.runtime)
    api(Dependencies.JakeWharton.converter_serialization)

    // Coroutines
    api(Dependencies.KotlinX.coroutines)

    // serialization
    api(Dependencies.KotlinX.serialization)
}