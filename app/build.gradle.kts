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
        getByName("debug") {
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
            minifyEnabled(false)
            zipAlignEnabled(true)
            debuggable(false)
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "HOST_LOGIN", "\"http://iot.ksf.com.cn:90/\"")
        }
        getByName("debug") {
            setSigningConfig(signingConfigs["debug"])
            debuggable(true)

            buildConfigField("String", "HOST_LOGIN", "\"http://114.116.18.154:8012/\"")
        }
    }

    android.applicationVariants.all {
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                outputFileName =
                    "${property("APP_NAME")}_V${versionNameOverride}_${
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
                                        "${project.property("APP_NAME")}_V${versionNameOverride}${
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
