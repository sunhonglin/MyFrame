buildscript {
    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath(Dependencies.Android.gradle)
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

            resolutionStrategy.force(
                "androidx.lifecycle:lifecycle-livedata:${dependenciesVersion(
                    rootProject,
                    "version.androidx.lifecycle"
                )}"
            )
            resolutionStrategy.force(
                "androidx.lifecycle:lifecycle-runtime:${dependenciesVersion(
                    rootProject,
                    "version.androidx.lifecycle"
                )}"
            )
            resolutionStrategy.force(
                "androidx.lifecycle:lifecycle-viewmodel:${dependenciesVersion(
                    rootProject,
                    "version.androidx.lifecycle"
                )}"
            )
            resolutionStrategy.force("androidx.arch.core:core-common:${Versions.archCore}")
            resolutionStrategy.force("androidx.arch.core:core-runtime:${Versions.archCore}")
            resolutionStrategy.force("androidx.appcompat:appcompat:${Versions.appcompat}")
            resolutionStrategy.force("org.jetbrains:annotations:${Versions.annotations}")
            resolutionStrategy.force(
                "com.google.code.gson:gson:${dependenciesVersion(
                    rootProject,
                    "version.com.google.code.gson..gson"
                )}"
            )
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}