object Versions {
    const val targetSdk = 29
    const val minSdk = 23
}

object Dependencies {
    object Android {
        const val gradle = "com.android.tools.build:gradle:_"
    }

    object Kotlin {
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:_"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:_"
    }

    object AndroidX{
        const val ktx = "androidx.core:core-ktx:_"
        const val appcompat = "androidx.appcompat:appcompat:_"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:_"
    }

    object KotlinX{

    }

    object Google {
        const val material = "com.google.android.material:material:_"
        const val gson = "com.google.code.gson:gson:_"
    }

    object Test{
        object AndroidX{
            const val junit = "androidx.test.ext:junit:_"
            object Espresso{
                const val core = "androidx.test.espresso:espresso-core:_"
            }
        }

        const val junit = "junit:junit:_"
    }

}