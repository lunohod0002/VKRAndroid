package com.example.vkr.data.network

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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