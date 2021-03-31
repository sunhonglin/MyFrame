package com.sunhonglin.myframe.data.login.model

data class LoginData(
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