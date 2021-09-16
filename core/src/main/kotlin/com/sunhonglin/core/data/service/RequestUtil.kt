package com.sunhonglin.core.data.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sunhonglin.core.BuildConfig
import com.sunhonglin.core.util.defaultJson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.Executors

class RequestUtil {
    companion object {

        val contentType = "application/json".toMediaType()

        inline fun <reified T> builder(
            baseUrl: String = "https://www.baidu.com/",
            vararg interceptors: Interceptor = emptyArray(),
            isStream: Boolean = false
        ): T {
            when (isStream) {
                true -> {
                    return Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .callbackExecutor(Executors.newSingleThreadExecutor())
                        .build()
                        .create(T::class.java)
                }
                else -> {
                    val interceptor = HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    }

                    val okHttpClientBuilder = OkHttpClient.Builder().addInterceptor(interceptor)

                    for (i in interceptors) {
                        okHttpClientBuilder.addInterceptor(i)
                    }

                    val build = okHttpClientBuilder.build()
                    return Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(defaultJson().asConverterFactory(contentType))
                        .callFactory(build)
                        .build()
                        .create(T::class.java)
                }
            }
        }
    }
}