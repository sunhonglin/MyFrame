plugins {
    kotlin("plugin.serialization")  version "1.8.10"
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
        maven(url = Repositories.REPO_MAVEN_HUAWEI)
    }

    val dirNameLast = path.substringBeforeLast(":").replace(":","\\")
    buildDir = File("${rootProject.buildDir}$dirNameLast", name)
    group = property("app.group").toString()
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}