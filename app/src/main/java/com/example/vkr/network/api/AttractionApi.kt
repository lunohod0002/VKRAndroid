package com.example.vkr.network.api

import com.example.vkr.logic.models.AttractionCreatedResponse
import com.example.vkr.logic.models.AttractionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AttractionApi {
    @POST("/api/attractions")
    suspend fun createAttraction(
        @Body request: AttractionRequest
    ): Response<AttractionCreatedResponse>
}