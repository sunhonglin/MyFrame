package com.sunhonglin.core.data.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sunhonglin.core.BuildConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RequestUtil {
    @ExperimentalSerializationApi
    companion object {
        val contentType = "application/json".toMediaType()
        fun <T> builder(t: Class<T>, baseUrl: String): T {
            val interceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(interceptor)

            val build = okHttpClientBuilder.build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(Json.asConverterFactory(contentType))
                .callFactory(build)
                .build()
                .create(t)
        }
    }
}