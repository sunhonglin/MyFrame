plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("android")
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        applicationId = "com.sunhonglin.myframe"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.AndroidX.ktx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.Google.material)
    implementation(Dependencies.AndroidX.constraintLayout)
    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.AndroidX.junit)
    androidTestImplementation(Dependencies.Test.AndroidX.Espresso.core)
}
