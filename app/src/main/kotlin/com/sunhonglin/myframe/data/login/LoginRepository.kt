package com.sunhonglin.myframe.data.login

import com.sunhonglin.core.data.service.BaseResponse
import com.sunhonglin.core.data.service.RequestResult
import com.sunhonglin.myframe.data.login.model.LoginData

class LoginRepository(
    private val loginDataSource: LoginDataSource
) {
    suspend fun toLogin(
        userName: String,
        password: String
    ): RequestResult<BaseResponse<LoginData>> {
        return loginDataSource.toLogin(
            userName = userName,
            password = password
        )
    }
}