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
    implementation(Dependencies.AndroidX.Lifecycle.viewModeSavedState)
    kapt(Dependencies.AndroidX.Lifecycle.compiler)

    // dagger
    implementation(Dependencies.Google.Dagger.dagger)
    kapt(Dependencies.Google.Dagger.compiler)
}