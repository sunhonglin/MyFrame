//package com.sunhonglin.myframe.data.login
//
//import com.sunhonglin.core.data.service.RequestResult
//import com.sunhonglin.core.util.safeApiCall
//import com.sunhonglin.myframe.data.login.model.LoginData
//import com.sunhonglin.myframe.data.api.LoginService
//import javax.inject.Inject
//
//class LoginDataSource @Inject constructor(private val loginService: LoginService) {
//
//    suspend fun toLogin(
//        userName: String,
//        password: String
//    ) = safeApiCall(
//        call = {
//            requestLogin(
//                userName = userName,
//                password = password
//            )
//        },
//        errorMessage = "这里是错误信息"
//    )
//
//    private suspend fun requestLogin(
//        userName: String,
//        password: String
//    ): RequestResult<LoginData> {
//        var result = loginService.toLogin(
//            userName = userName,
//            password = password
//        )
//        return RequestResult.Success(result)
//    }
//}