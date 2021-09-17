package com.sunhonglin.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sunhonglin.core.BuildConfig
import com.sunhonglin.core.data.service.RequestUtil
import com.sunhonglin.core.util.defaultJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter

/**
 * Hilt module to provide core data functionality.
 */
@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    fun providesJson(): Json = defaultJson()

    @Provides
    fun providesJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory(RequestUtil.contentType)

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    fun providesOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()
}