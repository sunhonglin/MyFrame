package com.sunhonglin.core.data.service

import com.sunhonglin.core.BuildConfig
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestUtil {
    companion object {
        private var headerInterceptor: Interceptor? = null

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

            headerInterceptor?.let {
                okHttpClientBuilder.addInterceptor(it)
            }
            val build = okHttpClientBuilder.build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .callFactory(build)
                .build()
                .create(t)
        }

        fun addHeaders(headers: Headers?) {
            if (headers == null) {
                headerInterceptor = null
                return
            }
            headerInterceptor = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    return chain.proceed(
                        chain.request().newBuilder()
                            .headers(headers).build()
                    )
                }
            }
        }
    }
}