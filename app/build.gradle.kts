apply("$rootDir/gradle/configure-android-defaults.gradle")

plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

var appName = property("APP_NAME")
android {
    defaultConfig {
        applicationId = group.toString()
        flavorDimensions("channel")
    }

    signingConfigs {
        create("release") {
            rootProject
            keyAlias(property(rootProject, KeystorePropertiesFilePath, "keyAlias").toString())
            keyPassword(property(rootProject, KeystorePropertiesFilePath, "keyPassword").toString())
            storeFile(
                rootProject.file(
                    property(
                        rootProject,
                        KeystorePropertiesFilePath,
                        "storeFile"
                    ).toString()
                )
            )
            storePassword(
                property(
                    rootProject,
                    KeystorePropertiesFilePath,
                    "storePassword"
                ).toString()
            )
            isV2SigningEnabled = true
        }
    }

    buildTypes {
        getByName("release") {
            setSigningConfig(signingConfigs["release"])
            minifyEnabled(true)
            zipAlignEnabled(true)
            debuggable(false)
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "HOST_LOGIN", "\"http://iot.ksf.com.cn:90/KSFReplaceApi/\"")
        }
        getByName("debug") {
            setSigningConfig(signingConfigs["release"])
            debuggable(true)
            buildConfigField("String", "HOST_LOGIN", "\"http://114.116.18.154:8012/\"")
        }
    }

    productFlavors {
        create("OfficialWebsite")//官网
        create("HuaWei")//华为
        create("XIaoMi")//小米
        create("YIngYongBao")//应用宝
    }
    productFlavors.all {
        manifestPlaceholders["CHANNEL_NAME"] = name
    }

    android.applicationVariants.all {
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                outputFileName =
                    "${appName}_V${versionName}_${
                        when (buildType) {
                            "release" -> {
                                "正式版"
                            }
                            "debug" -> "测试版"
                            else -> outputFileName
                        }
                    }_${systemDate()}${
                        when (!flavorName.isNullOrBlank()) {
                            true -> "_$flavorName"
                            else -> ""
                        }
                    }.apk"

                when (buildType == "release") {
                    true -> tasks.forEach {
                        if (it.name == ("assemble" + flavorName.capitalize() + "Release")) {
                            it.doLast {
                                copy {
                                    from("${buildDir}/outputs/apk/$flavorName/$buildType/$outputFileName")
                                    into("${rootDir.absolutePath}/apk/")
                                    rename {
                                        "${appName}_V${versionName}${
                                            when (!flavorName.isNullOrBlank()) {
                                                true -> "_$flavorName"
                                                else -> ""
                                            }
                                        }.apk"
                                    }
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
    implementation(Dependencies.Google.material)

    implementation(project(":core"))
    implementation(project(":base"))
    kapt(Dependencies.Google.Dagger.compiler)
    implementation(Dependencies.TenCent.bugLy)

    implementation(Dependencies.UMEng.UApp.common)
    implementation(Dependencies.UMEng.UApp.asMs)
}
