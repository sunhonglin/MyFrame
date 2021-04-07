apply("$rootDir/gradle/configure-android-defaults.gradle")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    api(Dependencies.AndroidX.appcompat)

    // room
    implementation(Dependencies.AndroidX.Room.runtime)
    api(Dependencies.AndroidX.Room.ktx)
    kapt(Dependencies.AndroidX.Room.compiler)

    // lifecycle
    api(Dependencies.AndroidX.Lifecycle.viewModel)
    api(Dependencies.AndroidX.Lifecycle.liveData)
    implementation(Dependencies.AndroidX.Lifecycle.runtime)
    api(Dependencies.AndroidX.Lifecycle.viewModeSavedState)
    kapt(Dependencies.AndroidX.Lifecycle.compiler)

    // dagger
    api(Dependencies.Google.Dagger.dagger)
    kapt(Dependencies.Google.Dagger.compiler)

    // retrofit2 + okHttp3
    api(Dependencies.SquareUp.OkHttp3.runtime)
    api(Dependencies.SquareUp.OkHttp3.loggingInterceptor)
    api(Dependencies.SquareUp.Retrofit2.runtime)
    api(Dependencies.SquareUp.Retrofit2.converter_gson)
    implementation(Dependencies.JakeWharton.converter_serialization)

    // Coroutines
    implementation(Dependencies.KotlinX.Coroutines.core)
    implementation(Dependencies.KotlinX.Coroutines.android)

    // serialization
    api(Dependencies.KotlinX.serialization)
}