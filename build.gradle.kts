plugins {
    kotlin("plugin.serialization")
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(Dependencies.Android.gradlePlugin)
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath(kotlin("serialization"))
        classpath(Dependencies.Google.Hilt.plugin)
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = Repositories.REPO_MAVEN_LANGCOO)
    }

    buildDir = File(rootProject.buildDir, name)
    group = property("app.group").toString()
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}