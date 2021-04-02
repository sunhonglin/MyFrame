apply("$rootDir/gradle/configure-android-defaults.gradle")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core"))

    api(Dependencies.QMUITeam.qmui)
    api(Dependencies.JakeWharton.timber)
    implementation(Dependencies.Google.gson)
}