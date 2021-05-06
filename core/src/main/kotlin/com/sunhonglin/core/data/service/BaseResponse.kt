package com.sunhonglin.core.data.service

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val code: Int? = 0,
    val success: Boolean,
    val result: T? = null,
    val message: String
)

fun <T> BaseResponse<T>.isSuccessful() = success