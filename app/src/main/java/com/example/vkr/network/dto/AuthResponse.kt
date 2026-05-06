package com.example.vkr.network.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)