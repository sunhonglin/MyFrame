buildscript {
    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath(Dependencies.Android.gradlePlugin)
        classpath(Dependencies.Kotlin.gradlePlugin)
    }
}

subprojects {
    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
    }

    buildDir = File(rootProject.buildDir, name)

    group = property("GROUP").toString()
    version = property("VERSION_NAME").toString()

    afterEvaluate {
        var kotlinVersion = dependenciesVersion(rootProject, "version.kotlin").toString()
        configurations.configureEach {
            // There could be transitive dependencies in tests with a lower version. This could cause
            // problems with a newer Kotlin version that we use.
            resolutionStrategy.force(Dependencies.Kotlin.reflect(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.common(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk8(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk7(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk6(kotlinVersion))

            resolutionStrategy.force(dependenciesUrl(rootProject, Dependencies.KotlinX.Coroutines.core, "version.kotlinx.coroutines"))
            resolutionStrategy.force(dependenciesUrl(rootProject, Dependencies.KotlinX.Coroutines.android, "version.kotlinx.coroutines"))
            resolutionStrategy.force(dependenciesUrl(rootProject, Dependencies.Google.gson, "version.com.google.code.gson..gson"))

            resolutionStrategy.force(dependenciesUrl(rootProject, "androidx.lifecycle:lifecycle-livedata:", "version.androidx.lifecycle"))
            resolutionStrategy.force(dependenciesUrl(rootProject, "androidx.lifecycle:lifecycle-runtime:", "version.androidx.lifecycle"))
            resolutionStrategy.force(dependenciesUrl(rootProject, "androidx.lifecycle:lifecycle-viewmodel:", "version.androidx.lifecycle"))

            resolutionStrategy.force(dependenciesUrl(rootProject, "androidx.core:core:", Versions.androidxCore))
            resolutionStrategy.force(dependenciesUrl(rootProject, "androidx.appcompat:appcompat:",Versions.appcompat))
            resolutionStrategy.force(dependenciesUrl(rootProject, "org.jetbrains:annotations:", Versions.annotations))
            resolutionStrategy.force(dependenciesUrl(rootProject, "androidx.arch.core:core-common:", Versions.archCore))
            resolutionStrategy.force(dependenciesUrl(rootProject, "androidx.arch.core:core-runtime:", Versions.archCore))
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}