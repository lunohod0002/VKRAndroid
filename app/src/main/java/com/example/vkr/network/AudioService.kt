package com.example.vkr.network;

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException
import java.io.InputStream


class AudioService : AudioPlayerApi {
    private val networkApi: AudioPlayerApi by lazy {
        Retrofit.Builder()
                .baseUrl(AudioPlayerApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AudioPlayerApi::class.java)
    }

//
//    override suspend fun getAudioStream(filename: String): Response<ResponseBody> {
//        return
//    }



}