package com.sunhonglin.myframe.data.login

import com.sunhonglin.base.utils.DataStoreUtil
import com.sunhonglin.core.util.decodeFromString
import com.sunhonglin.core.util.encodeToString
import com.sunhonglin.myframe.data.login.model.LoginData

object LoginDS {
    private const val LOGIN_INFO = "loginInfo"

    val loginData
        get() = LoginData.serializer().decodeFromString(DataStoreUtil.getSyncData(LOGIN_INFO, ""))

    fun saveLoginData(loginData: LoginData) =
        DataStoreUtil.putSyncData(LOGIN_INFO, loginData.encodeToString())
}