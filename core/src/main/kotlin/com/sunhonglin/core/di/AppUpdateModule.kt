package com.sunhonglin.core.di

import com.sunhonglin.core.data.api.AppUpdateService
import com.sunhonglin.core.data.appUpdate.AppUpdateDataSource
import com.sunhonglin.core.data.appUpdate.AppUpdateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AppUpdateModule {

    @Provides
    fun providesAppUpdateService(
        client: OkHttpClient,
        factory: Converter.Factory
    ): AppUpdateService {
        return Retrofit.Builder()
            .baseUrl("http://sfcss.langcoo.net:90")
            .callFactory(client.newBuilder().build())
            .addConverterFactory(factory)
            .build()
            .create(AppUpdateService::class.java)
    }

    @Provides
    fun providesAppUpdateRepository(dataSource: AppUpdateDataSource): AppUpdateRepository =
        AppUpdateRepository(dataSource)
}