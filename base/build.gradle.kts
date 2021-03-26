plugins {
    id("com.android.library")
    kotlin(module = "android")
}

apply("$rootDir/gradle/configure-android-defaults.gradle")

dependencies{
    implementation(Dependencies.QMUITeam.qmui)
}