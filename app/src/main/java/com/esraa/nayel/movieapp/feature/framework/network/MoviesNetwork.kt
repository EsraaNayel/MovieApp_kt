package com.esraa.nayel.movieapp.feature.framework.network

import com.esraa.nayel.movieapp.Constants
import com.esraa.nayel.movieapp.feature.data.remote.MovieRemoteDataSource
import com.esraa.nayel.movieapp.feature.framework.network.interceptors.AuthInterceptor
import com.esraa.nayel.movieapp.feature.framework.network.interceptors.LogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class MoviesNetwork {
    abstract fun dataSource(): MovieRemoteDataSource

    companion object {

        @Volatile
        private var INSTANCE: MoviesNetwork? = null
        fun getInstance(): MoviesNetwork {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: provideNetwork().also { INSTANCE = it }
            }
        }


        private fun provideNetwork(): MoviesNetwork {
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(Constants.API_KEY))
                .addInterceptor(LogInterceptor.debug())
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(Converter.json())
                .build()

            return object : MoviesNetwork() {
                override fun dataSource(): MovieRemoteDataSource {
                    return retrofit.create(MovieRemoteDataSource::class.java)
                }
            }
        }
    }
}