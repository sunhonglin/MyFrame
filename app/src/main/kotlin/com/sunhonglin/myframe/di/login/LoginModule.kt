package com.sunhonglin.myframe.di.login

import com.sunhonglin.myframe.data.login.LoginDataSource
import com.sunhonglin.myframe.data.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class LoginModule {

    @Provides
    fun providesLoginRepository(
        dataSource: LoginDataSource
    ): LoginRepository = LoginRepository(dataSource)
}