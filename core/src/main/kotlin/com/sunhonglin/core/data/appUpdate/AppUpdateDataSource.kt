package com.sunhonglin.core.data.appUpdate

import com.sunhonglin.core.data.api.AppUpdateService
import com.sunhonglin.core.data.appUpdate.model.AppUpdateInfo
import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.core.data.service.RequestResult
import com.sunhonglin.core.util.safeApiCall
import javax.inject.Inject

class AppUpdateDataSource @Inject constructor(
    private val appUpdateService: AppUpdateService
) {
    suspend fun toAppUpdate(
        applicationId: String,
        versionName: String,
        flavor: String
    ) = safeApiCall(
        call = {
            requestAppUpdate(
                applicationId = applicationId,
                versionName = versionName,
                flavor = flavor
            )
        },
        errorMessage = "获取更新失败"
    )

    private suspend fun requestAppUpdate(
        applicationId: String,
        versionName: String,
        flavor: String
    ): RequestResult<BaseResponse<AppUpdateInfo>> {
        val result = appUpdateService.toAppUpdate(
            applicationId = applicationId,
            versionName = versionName,
            flavor = flavor
        )
        return RequestResult.Success(result)
    }
}