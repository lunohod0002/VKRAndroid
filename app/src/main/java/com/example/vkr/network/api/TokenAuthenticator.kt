package com.example.vkr.network.api

import com.example.vkr.network.api.AuthApi
import com.example.vkr.network.dto.RefreshRequest
import com.example.vkr.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenStorage: TokenStorage,
    private val refreshApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.url.encodedPath.contains("/auth/")) return null

        if (responseCount(response) >= 2) {
            AuthEvents.emitLogout()
            return null
        }

        val refreshToken = runBlocking { tokenStorage.getRefreshToken() }
        if (refreshToken.isNullOrEmpty()) {
            AuthEvents.emitLogout()
            return null
        }

        val newTokens = runBlocking {
            runCatching { refreshApi.refresh(RefreshRequest(refreshToken)) }.getOrNull()
        }

        if (newTokens == null || !newTokens.isSuccessful || newTokens.body() == null) {
            runBlocking { tokenStorage.clear() }
            AuthEvents.emitLogout()
            return null
        }

        val body = newTokens.body()!!
        runBlocking { tokenStorage.refreshToken(body.accessToken) }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${body.accessToken}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var res: Response? = response.priorResponse
        var count = 1
        while (res != null) {
            count++
            res = res.priorResponse
        }
        return count
    }
}