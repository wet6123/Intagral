package com.ssafy.intagral.util

import com.ssafy.intagral.IntagralApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var value: String = if(IntagralApplication.prefs.token!=null) "Bearer "+IntagralApplication.prefs.token else ""
        var req = chain.request().newBuilder().addHeader("Authorization", value).build()
        return chain.proceed(req)
    }
}