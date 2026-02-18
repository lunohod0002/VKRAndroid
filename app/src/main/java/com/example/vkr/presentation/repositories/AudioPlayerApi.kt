package com.example.vkr.presentation.repositories


interface AudioPlayerApi {
//    @GET("/{file_id}")
//    suspend fun getAudioStream(@Path("file_id") filename: String): Response<ResponseBody>

    companion object {
        const val BASE_URL = "https://api-audio/test/"
    }
}