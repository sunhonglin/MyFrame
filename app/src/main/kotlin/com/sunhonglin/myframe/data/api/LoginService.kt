package com.sunhonglin.myframe.data.api

import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.myframe.data.login.model.ControllerReplaceType
import com.sunhonglin.myframe.data.login.model.LoginData
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface LoginService {

    /**
     * 登录
     */
    @POST("api/login")
    suspend fun toLogin(
        @Body params: Map<String, String>
    ): BaseResponse<LoginData>
}