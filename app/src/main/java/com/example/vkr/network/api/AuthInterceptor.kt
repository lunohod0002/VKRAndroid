package com.example.vkr.network.api

import com.example.vkr.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenStorage: TokenStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.url.encodedPath.contains("/auth/")) {
            return chain.proceed(original)
        }
        val token = runBlocking { tokenStorage.getAccessToken() }
        val request = if (token.isNullOrEmpty()) {
            original
        } else {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }
        return chain.proceed(request)
    }
}