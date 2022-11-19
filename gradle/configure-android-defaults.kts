android {
    namespace = "${project.property("app.id")}"
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        applicationId = "${project.property("app.id")}"
        versionCode = project.property("app.version.code").toString().toInteger()
        versionName = project.property("app.version.name").toString()
        println("appId -> $applicationId, app.version.code -> $versionCode, app.version.name -> $versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //If you want to tell the consumer not to obfuscate something from your library on their proguard process, include those in the consumer-rules.pro file.
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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