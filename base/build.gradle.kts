apply("$rootDir/gradle/configure-android-defaults.kts")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core"))
//    implementation(Dependencies.SunHongLin.core_ktx)
    // glide
    api(Dependencies.GitHub.Glide.runtime)
    kapt(Dependencies.GitHub.Glide.compiler)

    api(Dependencies.eventBus)
    api(Dependencies.QMUITeam.qmui)
    api(Dependencies.JakeWharton.timber)
    api(Dependencies.autoSize)
    implementation(Dependencies.AndroidX.Datastore.preferences)
}