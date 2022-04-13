object Dependencies {
    object Android {
        const val gradlePlugin = "com.android.tools.build:gradle:_"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:_"
        const val appcompat = "androidx.appcompat:appcompat:_"
        const val activity = "androidx.activity:activity-ktx:_"
        const val fragment = "androidx.fragment:fragment-ktx:_"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:_"
        const val customView = "androidx.customview:customview:_"
        const val drawerLayout = "androidx.drawerlayout:drawerlayout:_"
        const val loader = "androidx.loader:loader:_"
        const val viewPager2 = "androidx.viewpager2:viewpager2:_"

        object Annotation {
            const val runtime = "androidx.annotation:annotation:_"
            const val experimental = "androidx.annotation:annotation-experimental:_"
        }

        object Lifecycle {
            // ViewModel
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:_"

            // LiveData
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:_"

            // lifecycle only (without ViewModel or LiveData)
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:_"

            // Saved state module for ViewModel
            const val viewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:_"

            // Annotation processor kapt
            const val compiler = "androidx.lifecycle:lifecycle-compiler:_"
        }

        object Room {
            const val compiler = "androidx.room:room-compiler:_"
            const val ktx = "androidx.room:room-ktx:_"
        }

        object Hilt {
            const val compiler = "androidx.hilt:hilt-compiler:_"
            const val work = "androidx.hilt:hilt-work:_"
            const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:_"
        }

        object Datastore {
            const val core = "androidx.datastore:datastore:_"
            const val preferences = "androidx.datastore:datastore-preferences:_"
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
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:_"
    }

    object Google {
        const val material = "com.google.android.material:material:_"

        object Hilt {
            const val hilt = "com.google.dagger:hilt-android:_"
            const val compiler = "com.google.dagger:hilt-android-compiler:_"
            const val plugin = "com.google.dagger:hilt-android-gradle-plugin:_"
        }
    }

    object JakeWharton {
        const val timber = "com.jakewharton.timber:timber:_"
        const val converter_serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:_"
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

    object QMUITeam {
        const val qmui = "com.qmuiteam:qmui:_"
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

    object PERMISSION {
        const val acp = "com.mylhyl:acp:_"
    }

    object SunHongLin {
        const val core_ktx = "com.sunhonglin:core-ktx:_"
    }

    const val autoSize = "me.jessyan:autosize:_"
    const val eventBus = "org.greenrobot:eventbus:_"
}

fun main() {
    println(KotlinVersion.CURRENT.toString())
}