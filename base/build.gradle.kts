apply("$rootDir/gradle/configure-android-defaults.gradle")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core"))

    api(Dependencies.QMUITeam.qmui)
    api(Dependencies.JakeWharton.timber)
    implementation(Dependencies.Google.gson)

    // glide
    api(Dependencies.GitHub.Glide.runtime)
    kapt(Dependencies.GitHub.Glide.compiler)
}