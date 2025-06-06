package com.esraa.nayel.movieapp.feature.framework.network.interceptors

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

object LogInterceptor {

    fun debug(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    fun release(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        return loggingInterceptor
    }
}