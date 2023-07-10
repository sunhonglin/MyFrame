package com.sunhonglin.myframe.di

import com.sunhonglin.myframe.BuildConfig
import com.sunhonglin.myframe.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class ApiModule {

    @Provides
    fun providesApiService (
        client: OkHttpClient,
        factory: Converter.Factory
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST_LOGIN)
            .callFactory(client)
            .addConverterFactory(factory)
            .build()
            .create(ApiService::class.java)
    }
}