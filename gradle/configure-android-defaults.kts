android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode(VERSION_CODE.toInteger())
        versionName(VERSION_NAME)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //If you want to tell the consumer not to obfuscate something from your library on their proguard process, include those in the consumer-rules.pro file.
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            minifyEnabled(true)
            //Library will be obfuscated with the mentioned rules.
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "consumer-rules.pro")
        }
        debug {
            minifyEnabled(true)
            //Library will be obfuscated with the mentioned rules.
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "consumer-rules.pro")
        }
    }

    sourceSets {
        sourceSets["main"].java.srcDir("src/main/kotlin")
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}