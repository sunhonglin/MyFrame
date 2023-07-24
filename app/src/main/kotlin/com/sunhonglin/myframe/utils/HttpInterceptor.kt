package com.sunhonglin.myframe.utils

import com.sunhonglin.base.utils.EventBusUtil
import com.sunhonglin.core.util.getNodeValue
import com.sunhonglin.myframe.EventMessage
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

class HttpInterceptor : Interceptor {
    private var utf8 = Charset.forName("UTF-8")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
//        LoginDS.loginData?.let {
//            request = request.newBuilder()
//                .addHeader("Authorization", "${it.tokenHead} ${it.token}").build()
//        }

        val originalResponse = chain.proceed(request)

        try {
            originalResponse.body?.let {
                it.source().request(Long.MAX_VALUE)
                val bodyString =
                    it.source().buffer.clone().readString(it.contentType()?.charset(utf8) ?: utf8)


                when (bodyString.getNodeValue("code")) {
                    "401" -> EventBusUtil.sendEvent(EventMessage(EventMessage.LOGIN_GO))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return originalResponse
    }
}