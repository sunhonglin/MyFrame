plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply("$rootDir/gradle/configure-android-defaults.gradle")

dependencies{
    implementation(Dependencies.QMUITeam.qmui)
}