package com.sunhonglin.core.data.service

data class BaseResponse<T>(var code: Int, var success: Boolean, var result: T, var message: String)

fun <T> BaseResponse<T>.isSuccessful() = success