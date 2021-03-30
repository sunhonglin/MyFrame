//package com.sunhonglin.myframe.data.login
//
//import com.sunhonglin.core.data.service.RequestResult
//import com.sunhonglin.myframe.data.login.model.LoginData
//import java.io.IOException
//
//class LoginRepository(private val loginDataSource: LoginDataSource) {
//
//    suspend fun toLogin(
//        userName: String,
//        password: String
//    ) : RequestResult<LoginData> {
//        var result = loginDataSource.toLogin(
//            userName = userName,
//            password = password
//        )
//        if (result is RequestResult.Success) {
//            return result
//        }
//        return RequestResult.Error(IOException("Unable to post login"))
//    }
//
//}