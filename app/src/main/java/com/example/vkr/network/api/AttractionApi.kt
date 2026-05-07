package com.example.vkr.network.api

import com.example.vkr.network.dto.AttractionCreatedResponse
import com.example.vkr.network.dto.AttractionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AttractionApi {
    @POST("/api/attractions")
    suspend fun createAttraction(
        @Body request: AttractionRequest
    ): Response<AttractionCreatedResponse>
}