package com.example.vkr.network

import com.example.vkr.network.api.AuthApi
import com.example.vkr.network.api.AuthInterceptor
import com.example.vkr.network.api.StationRepository
import com.example.vkr.storage.TokenStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.20:8080"

    private fun okHttp(tokenStorage: TokenStorage): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStorage))
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private fun retrofit(tokenStorage: TokenStorage): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp(tokenStorage))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun authApi(tokenStorage: TokenStorage): AuthApi =
        retrofit(tokenStorage).create(AuthApi::class.java)

    fun stationApi(tokenStorage: TokenStorage): StationRepository =
        retrofit(tokenStorage).create(StationRepository::class.java)
}