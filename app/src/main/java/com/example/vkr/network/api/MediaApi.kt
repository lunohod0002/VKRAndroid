package com.example.vkr.network.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MediaApi {
    @Multipart
    @POST("/api/medias/upload")
    suspend fun upload(
        @Part files: List<MultipartBody.Part>
    ): Response<List<String>>
}