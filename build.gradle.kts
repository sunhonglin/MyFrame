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

    println(group)
    println(version)

    group = property("GROUP").toString()
    version = property("VERSION_NAME").toString()

    println(group)
    println(version)

    afterEvaluate {
        var kotlinVersion = Utils.getVersionByName("version.kotlin").toString()
        configurations.configureEach {
            // There could be transitive dependencies in tests with a lower version. This could cause
            // problems with a newer Kotlin version that we use.
            resolutionStrategy.force(Dependencies.Kotlin.reflect(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.common(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk8(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk7(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk6(kotlinVersion))

            resolutionStrategy.force("androidx.lifecycle:lifecycle-livedata:${Utils.getVersionByName("version.androidx.lifecycle")}")
            resolutionStrategy.force("androidx.lifecycle:lifecycle-runtime:${Utils.getVersionByName("version.androidx.lifecycle")}")
            resolutionStrategy.force("androidx.lifecycle:lifecycle-viewmodel:${Utils.getVersionByName("version.androidx.lifecycle")}")
            resolutionStrategy.force("androidx.arch.core:core-common:${Versions.archCore}")
            resolutionStrategy.force("androidx.arch.core:core-runtime:${Versions.archCore}")
        }
    }

}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}