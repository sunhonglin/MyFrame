package com.sunhonglin.core.di

import com.google.gson.Gson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sunhonglin.core.BuildConfig
import com.sunhonglin.core.data.service.RequestUtil
import com.sunhonglin.core.di.scope.AppScope
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Dagger module to provide core data functionality.
 */
@Module
class CoreDataModule {
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }


    @Provides
    @AppScope
    fun provideJson(): Json = Json

    @Provides
    @AppScope
    @ExperimentalSerializationApi
    fun provideJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory(RequestUtil.contentType)
}