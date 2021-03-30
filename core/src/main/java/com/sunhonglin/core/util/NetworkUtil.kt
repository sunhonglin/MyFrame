package com.sunhonglin.core.util

import com.sunhonglin.core.data.service.RequestResult
import java.io.IOException

suspend fun <T : Any> safeApiCall(
    call: suspend () -> RequestResult<T>,
    errorMessage: String
): RequestResult<T> {
    return try {
        call()
    } catch (e: Exception) {
        RequestResult.Error(IOException(errorMessage, e))
    }
}