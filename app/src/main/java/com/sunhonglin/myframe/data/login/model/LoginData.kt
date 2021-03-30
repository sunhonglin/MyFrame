package com.sunhonglin.myframe.data.login.model

import androidx.annotation.NonNull
import com.sunhonglin.base.utils.GsonUtil
import org.jetbrains.annotations.NotNull
import java.io.Serializable

class LoginData(
    /**
     * token
     */
    var token: String,

    /**
     * 用户名
     */
    var userName: String,

    /**
     * 用户类型
     * 1：松下 2：海信
     */
    var type: Int = 0,

    /**
     * 账号对应的厂商手持机串号
     */
    var imei: String
)

fun main(args: Array<String>) {
    var str = "{\"token\":\"123\",\"userName\":null}"
    var loginData = GsonUtil.GsonToBean(str, LoginData::class.java)
    println(GsonUtil.toGson(loginData))
}