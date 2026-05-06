package com.example.vkr.network.api

import com.example.vkr.network.dto.AuthResponse
import com.example.vkr.network.dto.LoginRequest
import com.example.vkr.network.dto.RefreshRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @POST("/auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): Response<AuthResponse>
}