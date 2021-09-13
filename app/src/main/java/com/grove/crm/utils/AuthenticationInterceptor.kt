package com.grove.crm.utils

import com.grove.crm.MyApplication
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor(private val token: String?): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val builder: Request.Builder = original.newBuilder()
            .header("lang", "${LocaleManager.getLocale(MyApplication.instance!!.resources)}")
        token?.let {
            builder.header("Authorization", "Bearer $token" ?: "")
        }

        val request: Request = builder.build()
        return chain.proceed(request)
    }
}