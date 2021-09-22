package com.sunhonglin.core.data.api

import com.sunhonglin.core.data.appUpdate.model.AppUpdateInfo
import com.sunhonglin.core.data.service.BaseResponse
import dagger.Provides
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface AppUpdateService {

    /**
     * 检查更新
     */
    @GET("http://sfcss.langcoo.net:90/rest/v/{applicationId}-{versionName}-{flavor}-android")
    suspend fun toAppUpdate(
        @Path("applicationId") applicationId: String,
        @Path("versionName") versionName: String,
        @Path("flavor") flavor: String
    ): BaseResponse<AppUpdateInfo>

    @GET
    @Streaming
    fun toDownLoadApk(
        @Url url: String
    ): Call<ResponseBody>
}