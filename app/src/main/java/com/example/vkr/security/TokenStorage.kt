package com.example.vkr.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vkr.security.CryptoManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

class TokenStorage(private val context: Context) {

    private val crypto = CryptoManager()

    private val accessKey = stringPreferencesKey("access_token")
    private val refreshKey = stringPreferencesKey("refresh_token")

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        val encAccess = crypto.encrypt(accessToken)
        val encRefresh = crypto.encrypt(refreshToken)
        context.authDataStore.edit { prefs ->
            prefs[accessKey] = encAccess
            prefs[refreshKey] = encRefresh
        }
    }

    val accessTokenFlow: Flow<String?> = context.authDataStore.data.map { prefs ->
        prefs[accessKey]?.let { runCatching { crypto.decrypt(it) }.getOrNull() }
    }

    val refreshTokenFlow: Flow<String?> = context.authDataStore.data.map { prefs ->
        prefs[refreshKey]?.let { runCatching { crypto.decrypt(it) }.getOrNull() }
    }

    suspend fun getAccessToken(): String? = accessTokenFlow.first()
    suspend fun getRefreshToken(): String? = refreshTokenFlow.first()

    suspend fun clear() {
        context.authDataStore.edit { it.clear() }
    }
}