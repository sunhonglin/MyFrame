plugins {
    kotlin("plugin.serialization")
}

buildscript {
    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
        maven("https://dl.bintray.com/umsdk/release")
    }
    dependencies {
        classpath(Dependencies.Android.gradlePlugin)
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath(kotlin("serialization"))
    }
}

val kotlinVersion = dependenciesVersion(rootProject, "version.kotlin").toString()
val coroutinesCore = dependenciesUrl(rootProject, Dependencies.KotlinX.Coroutines.core, "version.kotlinx.coroutines")
val coroutinesAndroid = dependenciesUrl(rootProject, Dependencies.KotlinX.Coroutines.android, "version.kotlinx.coroutines")
val gson = dependenciesUrl(rootProject, Dependencies.Google.gson, "version.com.google.code.gson..gson")
val lifecycleLiveData = dependenciesUrl(rootProject, "androidx.lifecycle:lifecycle-livedata:", "version.androidx.lifecycle")
val lifecycleRuntime = dependenciesUrl(rootProject, "androidx.lifecycle:lifecycle-runtime:", "version.androidx.lifecycle")
val lifecycleViewModel = dependenciesUrl(rootProject, "androidx.lifecycle:lifecycle-viewmodel:", "version.androidx.lifecycle")
val androidxCore = dependenciesUrl(rootProject, "androidx.core:core:", Versions.androidxCore)
val appcompat = dependenciesUrl(rootProject, "androidx.appcompat:appcompat:",Versions.appcompat)
val annotations = dependenciesUrl(rootProject, "org.jetbrains:annotations:", Versions.annotations)
val coreCommon = dependenciesUrl(rootProject, "androidx.arch.core:core-common:", Versions.archCore)
val coreRuntime = dependenciesUrl(rootProject, "androidx.arch.core:core-runtime:", Versions.archCore)

subprojects {
    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
        maven("https://dl.bintray.com/umsdk/release")
    }

    buildDir = File(rootProject.buildDir, name)
    group = property("GROUP").toString()
    version = property("VERSION_NAME").toString()

    afterEvaluate {
        configurations.configureEach {
            // There could be transitive dependencies in tests with a lower version. This could cause
            // problems with a newer Kotlin version that we use.
            resolutionStrategy.force(Dependencies.Kotlin.reflect(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.common(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk8(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk7(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk6(kotlinVersion))

            resolutionStrategy.force(coroutinesCore)
            resolutionStrategy.force(coroutinesAndroid)
            resolutionStrategy.force(gson)

            resolutionStrategy.force(lifecycleLiveData)
            resolutionStrategy.force(lifecycleRuntime)
            resolutionStrategy.force(lifecycleViewModel)

            resolutionStrategy.force(androidxCore)
            resolutionStrategy.force(appcompat)
            resolutionStrategy.force(annotations)
            resolutionStrategy.force(coreCommon)
            resolutionStrategy.force(coreRuntime)
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}