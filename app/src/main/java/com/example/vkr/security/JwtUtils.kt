package com.example.vkr.security

import android.util.Base64
import org.json.JSONObject

object JwtUtils {

    /**
     * Возвращает true, если токен валиден по структуре и срок действия не истёк.
     * Сравнение делается с небольшим запасом (leeway), чтобы не "пограничить".
     */
    fun isValid(token: String?, leewaySeconds: Long = 10): Boolean {
        val exp = expiresAtSeconds(token) ?: return false
        val nowSec = System.currentTimeMillis() / 1000
        return exp - leewaySeconds > nowSec
    }

    /** Возвращает значение поля `exp` (в секундах) или null, если распарсить не удалось. */
    fun expiresAtSeconds(token: String?): Long? {
        if (token.isNullOrBlank()) return null
        val parts = token.split(".")
        if (parts.size < 2) return null
        return try {
            val payload = String(
                Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING),
                Charsets.UTF_8
            )
            val json = JSONObject(payload)
            if (json.has("exp")) json.getLong("exp") else null
        } catch (e: Exception) {
            null
        }
    }
}