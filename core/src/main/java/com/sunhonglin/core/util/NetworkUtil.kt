package com.sunhonglin.core.util

import com.sunhonglin.core.data.service.RequestResult
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

suspend fun <T : Any> safeApiCall(
    call: suspend () -> RequestResult<T>,
    errorMessage: String
): RequestResult<T> {
    return try {
        call()
    } catch (e: SocketException) {
        e.printStackTrace()
        RequestResult.Error(IOException(errorMessage, e))
    } catch (e: ConnectException) {
        e.printStackTrace()
        RequestResult.Error(IOException(errorMessage, e))
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        RequestResult.Error(IOException(errorMessage, e))
    } catch (e: Exception) {
        e.printStackTrace()
        RequestResult.Error(IOException(errorMessage, e))
    }
}