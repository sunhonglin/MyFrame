import java.io.File
import java.util.*

object Versions {
    const val targetSdk = 29
    const val minSdk = 23
    const val archCore = "2.1.0"
}

object Utils {
    fun getVersionByName(name: String): Any? {
        val keystorePropertiesFile = File("versions.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(keystorePropertiesFile.inputStream())
        return keystoreProperties[name]
    }
}

object Dependencies {
    object Android {
        const val gradle = "com.android.tools.build:gradle:_"
    }

    object AndroidX {
        const val ktx = "androidx.core:core-ktx:_"
        const val appcompat = "androidx.appcompat:appcompat:_"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:_"

        object Room {
            const val runtime = "androidx.room:room-runtime:_"
            const val compiler = "androidx.room:room-compiler:_"
            const val ktx = "androidx.room:room-ktx:_"
        }

        object Lifecycle {
            // ViewModel
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:_"

            // LiveData
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:_"

            // Lifecycles only (without ViewModel or LiveData)
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:_"

            // Saved state module for ViewModel
            const val viewModeSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:_"

            // Annotation processor kapt
            const val compiler = "androidx.lifecycle:lifecycle-compiler:_"
        }
    }

    object Kotlin {
        val gradlePlugin = kotlin("gradle-plugin", "_")

        private fun kotlin(module: String, version: String) =
            "org.jetbrains.kotlin:kotlin-$module:$version"

        fun reflect(version: String) = kotlin("reflect", version)

        object Stdlib {
            fun common(version: String) = kotlin("stdlib-common", version)
            fun jdk8(version: String) = kotlin("stdlib-jdk8", version)
            fun jdk7(version: String) = kotlin("stdlib-jdk7", version)
            fun jdk6(version: String) = kotlin("stdlib", version)
        }
    }

    object KotlinX {

    }

    object Google {
        const val material = "com.google.android.material:material:_"
        const val gson = "com.google.code.gson:gson:_"

        object Dagger {
            const val dagger = "com.google.dagger:dagger:_"
            const val compiler = "com.google.dagger:dagger-compiler:_"
        }
    }

    object QMUITeam {
        const val qmui = "com.qmuiteam:qmui:_"
    }

    object Test {
        object AndroidX {
            const val junit = "androidx.test.ext:junit:_"

            object Espresso {
                const val core = "androidx.test.espresso:espresso-core:_"
            }
        }

        const val junit = "junit:junit:_"
    }

}