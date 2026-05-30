package com.example.vkr.network

import com.example.vkr.network.api.AttractionApi
import com.example.vkr.network.api.MediaApi
import com.example.vkr.network.api.StationApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    const val BASE_URL = "http://192.168.1.20:8080"



    private fun okHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.20:8080")
        .client(okHttp())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun stationApi(): StationApi =
        retrofit().create(StationApi::class.java)

    fun mediaApi(): MediaApi =
        retrofit().create(MediaApi::class.java)

    fun attractionApi(): AttractionApi =
        retrofit().create(AttractionApi::class.java)
}