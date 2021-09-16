package com.sunhonglin.core.data.appUpdate

import com.sunhonglin.core.data.appUpdate.model.AppUpdateInfo
import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.core.data.service.RequestResult
import javax.inject.Inject

class AppUpdateRepository (
    private val appUpdateDataSource: AppUpdateDataSource
) {

    suspend fun toAppUpdate(
        applicationId: String,
        versionName: String,
        flavor: String
    ): RequestResult<BaseResponse<AppUpdateInfo>> {
        return appUpdateDataSource.toAppUpdate(
            applicationId = applicationId,
            versionName = versionName,
            flavor = flavor
        )
    }
}