apply("$rootDir/gradle/configure-android-defaults.kts")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.google.dagger.hilt.android")
}

var appName = property("APP_NAME")
android {
    defaultConfig {
        applicationId = group.toString()
        flavorDimensions.add("channel")
    }

    signingConfigs {
        create("release") {
            keyAlias = property(rootProject, KeystorePropertiesFilePath, "key.alias").toString()
            keyPassword =
                property(rootProject, KeystorePropertiesFilePath, "key.password").toString()
            storeFile =
                rootProject.file(
                    property(
                        rootProject,
                        KeystorePropertiesFilePath,
                        "key.store.file"
                    ).toString()
                )
            storePassword =
                property(
                    rootProject,
                    KeystorePropertiesFilePath,
                    "key.store.password"
                ).toString()
            enableV2Signing = true
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "HOST_LOGIN", "\"http://iot.ksf.com.cn:90/KSFReplaceApi/\"")
        }
        getByName("debug") {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "HOST_LOGIN", "\"http://iot.ksf.com.cn:90/KSFReplaceApi/\"")
        }
    }

    productFlavors {
        create("official")//官网
//        create("HuaWei")//华为
    }
    productFlavors.all {
        manifestPlaceholders["CHANNEL_NAME"] = name
    }

    android.applicationVariants.all {
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                outputFileName =
                    "${appName}_${buildType}_V${versionName}_${systemDate()}${
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
}
