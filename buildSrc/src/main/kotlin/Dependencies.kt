object Dependencies {
    object Android {
        const val gradlePlugin = "com.android.tools.build:gradle:_"
    }

    object AndroidX {
        const val ktx = "androidx.core:core-ktx:_"
        const val appcompat = "androidx.appcompat:appcompat:_"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:_"

        object Activity {
            const val runtime = "androidx.activity:activity:_"
            const val ktx = "androidx.activity:activity-ktx:_"
        }

        object Fragment {
            const val runtime = "androidx.fragment:fragment:_"
            const val ktx = "androidx.fragment:fragment-ktx:_"
        }

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

            // lifecycle only (without ViewModel or LiveData)
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
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:_"

        object Coroutines {
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:_"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:_"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:_"
        const val gson = "com.google.code.gson:gson:_"

        object Dagger {
            const val dagger = "com.google.dagger:dagger:_"
            const val compiler = "com.google.dagger:dagger-compiler:_"
        }
    }

    object JakeWharton {
        const val timber = "com.jakewharton.timber:timber:_"
        const val converter_serialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:_"
    }

    object QMUITeam {
        const val qmui = "com.qmuiteam:qmui:_"
    }

    object SquareUp {
        object Retrofit2 {
            const val runtime = "com.squareup.retrofit2:retrofit:_"
            const val converter_gson = "com.squareup.retrofit2:converter-gson:_"
        }

        object OkHttp3 {
            const val runtime = "com.squareup.okhttp3:okhttp:_"
            const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:_"
        }
    }

    object GitHub {
        object Glide {
            const val runtime = "com.github.bumptech.glide:glide:_"
            const val compiler = "com.github.bumptech.glide:compiler:_"
        }
    }

    object TenCent {
        const val bugLy = "com.tencent.bugly:crashreport:_"
    }

    object UMEng {
        object UApp {
            const val common = "com.umeng.umsdk:common:_"
            const val asMs = "com.umeng.umsdk:asms:_"
        }
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