package com.sunhonglin.core.data.service

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class BaseResponse<T>(
    val code: Int? = 0,
    val success: Boolean,
    val data: T? = null,
    val message: String = ""
)

fun <T> BaseResponse<T>.isSuccessful() = success