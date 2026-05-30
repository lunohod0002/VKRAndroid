package com.example.vkr.di

import com.example.vkr.network.api.AttractionApi
import com.example.vkr.network.api.MediaApi
import com.example.vkr.network.api.StationApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    private fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.20:8080")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideStationApi(retrofit: Retrofit): StationApi =
        retrofit.create(StationApi::class.java)
    @Provides
    @Singleton
    fun mediaApi(retrofit: Retrofit): MediaApi =
        retrofit.create(MediaApi::class.java)

    fun attractionApi(retrofit: Retrofit): AttractionApi =
        retrofit.create(AttractionApi::class.java)
}