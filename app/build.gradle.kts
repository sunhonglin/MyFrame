plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("android")
}

apply("$rootDir/gradle/configure-android-defaults.gradle")

android {
    defaultConfig {
        applicationId = property("GROUP").toString()
    }

//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
}

dependencies {
//    implementation(Dependencies.Kotlin.stdLib)
//    implementation(Dependencies.AndroidX.ktx)
//    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.Google.material)
//    implementation(Dependencies.AndroidX.constraintLayout)

    implementation(project(":core"))
    implementation(project(":base"))
//    testImplementation(Dependencies.Test.junit)
//    androidTestImplementation(Dependencies.Test.AndroidX.junit)
//    androidTestImplementation(Dependencies.Test.AndroidX.Espresso.core)
}
