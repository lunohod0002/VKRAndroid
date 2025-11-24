package com.example.vkr.data.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming


interface AudioPlayerApi {
//    @GET("/{file_id}")
//    suspend fun getAudioStream(@Path("file_id") filename: String): Response<ResponseBody>

    companion object {
        const val BASE_URL = "https://api-audio/test/"
    }
}