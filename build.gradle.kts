buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Dependencies.Android.gradle)
        classpath(Dependencies.Kotlin.gradlePlugin)
    }
}

subprojects {
    repositories {
        google()
        jcenter()
    }

    buildDir = File(rootProject.buildDir, name)
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}