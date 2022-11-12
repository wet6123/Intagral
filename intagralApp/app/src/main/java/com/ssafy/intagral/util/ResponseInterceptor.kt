package com.ssafy.intagral.util

import com.ssafy.intagral.IntagralApplication
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            401 -> {
                // 토큰 삭제
                IntagralApplication.prefs.token = "EXPIRED";
            }
        }
        return response
    }
}