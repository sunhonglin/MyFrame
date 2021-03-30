plugins {
    id("com.android.library")
    kotlin(module = "android")
}

apply("$rootDir/gradle/configure-android-defaults.gradle")

dependencies {
    implementation(project(":core"))
    api(Dependencies.QMUITeam.qmui)
    implementation(Dependencies.Google.gson)
    api(Dependencies.JakeWharton.timber)
}