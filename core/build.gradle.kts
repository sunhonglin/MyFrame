plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
}

apply("$rootDir/gradle/configure-android-defaults.gradle")

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
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

    // retrofit
    api(Dependencies.SquareUp.OkHttp3.runtime)
    api(Dependencies.SquareUp.OkHttp3.loggingInterceptor)
    api(Dependencies.SquareUp.Retrofit2.runtime)
    api(Dependencies.SquareUp.Retrofit2.converter_gson)

}