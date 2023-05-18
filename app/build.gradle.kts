apply("$rootDir/gradle/configure-android-defaults.kts")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.google.dagger.hilt.android")
}

android {
    signingConfigs {
        create("release") {
            keyAlias = property(rootProject, KeystorePropertiesFilePath, "key.alias").toString()
            keyPassword = property(rootProject, KeystorePropertiesFilePath, "key.password").toString()
            storeFile = rootProject.file(property(rootProject, KeystorePropertiesFilePath, "key.store.file").toString())
            storePassword = property(rootProject, KeystorePropertiesFilePath, "key.store.password").toString()
            enableV2Signing = true
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            buildConfigField("String", "HOST_LOGIN", "\"http://iot.langcoo.net:8012/after/sale/service/center/\"")
        }
        getByName("debug") {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            buildConfigField("String", "HOST_LOGIN", "\"http://iottest.langcoo.net:8012/after/sale/service/center/\"")
        }
    }

    android.applicationVariants.all {
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                outputFileName =
                    "${property("app.name")}_${buildType}_V${versionName}_${systemDate()}${
                        when (!flavorName.isNullOrBlank()) {
                            true -> "_$flavorName"
                            else -> ""
                        }
                    }.apk"
                when (buildType) {
                    "release" -> {
                        tasks.getByName("assemble${flavorName.capitalize()}Release") {
                            this.doLast {
                                copy {
                                    from("${buildDir}/outputs/apk/$flavorName/$buildType/$outputFileName")
                                    into("${rootDir.absolutePath}/apk/")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

//    productFlavors {
//        create("official")//官网
////        create("HuaWei")//华为
//    }
//    productFlavors.all {
//        manifestPlaceholders["CHANNEL_NAME"] = name
//    }
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(
        fileTree(baseDir = "libs") {
            include("*.jar")
            include("*.aar")
        }
    )

    implementation(Dependencies.SunHongLin.core_ktx)
    implementation(Dependencies.SunHongLin.base_ktx)

    implementation(Dependencies.Google.Hilt.hilt)
    kapt(Dependencies.Google.Hilt.compiler)
    kapt(Dependencies.AndroidX.Hilt.compiler)

    implementation(Dependencies.TenCent.bugLy)

    // room
    api(Dependencies.AndroidX.Room.ktx)
    kapt(Dependencies.AndroidX.Room.compiler)
}
