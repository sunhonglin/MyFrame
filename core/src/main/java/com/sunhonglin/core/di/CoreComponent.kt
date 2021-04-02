package com.sunhonglin.core.di

import android.content.Context
import com.google.gson.Gson
import com.haier.detect.android.core.di.qualifiers.ApplicationContext
import com.sunhonglin.core.di.scope.AppScope
import dagger.BindsInstance
import dagger.Component
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Component providing application wide singletons.
 * To call this make use of MedKeyApplication.coreComponent or the
 * Activity.coreComponent extension function.
 */
@Component(modules = [CoreDataModule::class])
@AppScope
interface CoreComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@ApplicationContext context: Context): Builder
        fun build(): CoreComponent
    }

    fun provideOkHttpClient(): OkHttpClient
    fun provideLoggingInterceptor(): HttpLoggingInterceptor
    fun provideJson(): Json
    fun provideJsonConverterFactory(): Converter.Factory

    @ApplicationContext
    fun provideApplicationContext(): Context
}