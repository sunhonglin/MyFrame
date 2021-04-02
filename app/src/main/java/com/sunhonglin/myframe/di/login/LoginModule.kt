package com.sunhonglin.myframe.di.login

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.sunhonglin.core.di.scope.FeatureScope
import com.sunhonglin.myframe.BuildConfig
import com.sunhonglin.myframe.data.api.LoginService
import com.sunhonglin.myframe.data.login.LoginDataSource
import com.sunhonglin.myframe.data.login.LoginRepository
import com.sunhonglin.myframe.ui.login.LoginActivity
import com.sunhonglin.myframe.ui.login.LoginViewModel
import com.sunhonglin.myframe.ui.login.LoginViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class LoginModule {

    @Binds
    abstract fun loginActivityAsFragmentActivity(activity: LoginActivity): FragmentActivity



    companion object {

        @JvmStatic
        @Provides
        fun provideLoginViewModel(
            fragmentActivity: FragmentActivity,
            factory: LoginViewModelFactory
        ): LoginViewModel {
            return ViewModelProvider(fragmentActivity, factory)[LoginViewModel::class.java]
        }

        @Provides
        @FeatureScope
        fun provideLoginService(
            client: OkHttpClient,
            factory: GsonConverterFactory
        ): LoginService {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.HOST_LOGIN)
                .callFactory(client)
                .addConverterFactory(factory)
                .build()
                .create(LoginService::class.java)
        }

        @Provides
        @FeatureScope
        fun provideLoginRepository(dataSource: LoginDataSource): LoginRepository =
            LoginRepository(dataSource)
    }

}