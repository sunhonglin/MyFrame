package com.sunhonglin.core.util

import android.text.TextUtils
import com.sunhonglin.core.data.service.RequestResult
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

suspend fun <T : Any> safeApiCall(
    call: suspend () -> RequestResult<T>,
    errorMessage: String
): RequestResult<T> {
    return try {
        call()
    } catch (e: Exception) {
        e.printStackTrace()
        when {
            e is SocketTimeoutException || (e is InterruptedIOException && TextUtils.equals(
                e.message,
                "timeout"
            )) -> RequestResult.Error(IOException("$errorMessage：请求超时", e))
            else -> RequestResult.Error(IOException("$errorMessage：网络请求异常", e))
        }
    }
}