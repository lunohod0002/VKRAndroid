package com.example.vkr.network

import com.example.vkr.network.api.AttractionApi
import com.example.vkr.network.api.AuthApi
import com.example.vkr.network.api.AuthInterceptor
import com.example.vkr.network.api.MediaApi
import com.example.vkr.network.api.StationApi
import com.example.vkr.network.api.TokenAuthenticator
import com.example.vkr.storage.TokenStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    const val BASE_URL = "http://192.168.1.20:8080"


    private val cleanClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val cleanRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cleanClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authApi: AuthApi by lazy { cleanRetrofit.create(AuthApi::class.java) }

    private fun okHttp(tokenStorage: TokenStorage): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStorage))
            .authenticator(TokenAuthenticator(tokenStorage, authApi))
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun retrofit(tokenStorage: TokenStorage): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp(tokenStorage))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun authApi(): AuthApi = authApi

    fun stationApi(tokenStorage: TokenStorage): StationApi =
        retrofit(tokenStorage).create(StationApi::class.java)

    fun mediaApi(tokenStorage: TokenStorage): MediaApi =
        retrofit(tokenStorage).create(MediaApi::class.java)

    fun attractionApi(tokenStorage: TokenStorage): AttractionApi =
        retrofit(tokenStorage).create(AttractionApi::class.java)
}