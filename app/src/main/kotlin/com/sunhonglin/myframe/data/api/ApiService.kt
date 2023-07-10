package com.sunhonglin.myframe.data.api

import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.myframe.data.login.model.LoginData
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    /**
     * 登录
     */
    @POST("login")
    suspend fun toLogin(
        @Body params: Map<String, String>
    ): BaseResponse<LoginData>
}