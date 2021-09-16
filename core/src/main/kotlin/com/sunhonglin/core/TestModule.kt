package com.sunhonglin.core

import com.sunhonglin.core.data.api.AppUpdateService
import com.sunhonglin.core.data.appUpdate.AppUpdateDataSource
import com.sunhonglin.core.data.appUpdate.AppUpdateRepository
import com.sunhonglin.core.data.service.RequestUtil
import com.sunhonglin.core.di.CoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class TestModule: CoreModule() {

    @Provides
    fun providesAppUpdateService(): AppUpdateService = RequestUtil.builder()

    @Provides
    fun providesAppUpdateDataSource(
        service: AppUpdateService
    ) = AppUpdateDataSource(service)

    @Provides
    fun providesAppUpdateRepository(
        dataSource: AppUpdateDataSource
    ) = AppUpdateRepository(dataSource)
}