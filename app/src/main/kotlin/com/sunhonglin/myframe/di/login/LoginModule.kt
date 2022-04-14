package com.sunhonglin.myframe.di.login

import com.sunhonglin.myframe.BuildConfig
import com.sunhonglin.myframe.data.api.LoginService
import com.sunhonglin.myframe.data.login.LoginDataSource
import com.sunhonglin.myframe.data.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class LoginModule {

    @Inject
    @Provides
    fun providesLoginService (
        client: OkHttpClient,
        factory: Converter.Factory
    ): LoginService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST_LOGIN)
            .callFactory(client)
            .addConverterFactory(factory)
            .build()
            .create(LoginService::class.java)
    }

    @Provides
    fun providesLoginRepository(
        dataSource: LoginDataSource
    ): LoginRepository = LoginRepository(dataSource)
}