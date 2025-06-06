package com.esraa.nayel.movieapp.feature.framework.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor( private val token: String): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()
        if (token.isNotEmpty()) {
            newRequestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(newRequestBuilder.build())
    }
}