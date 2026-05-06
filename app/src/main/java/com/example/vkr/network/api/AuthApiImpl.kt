package com.example.vkr.network.api

import com.example.vkr.network.api.AuthApi
import com.example.vkr.network.dto.LoginRequest
import com.example.vkr.storage.TokenStorage

class AuthApiImpl(
        private val authApi: AuthApi,
        private val tokenStorage: TokenStorage
) {
    sealed class Result {
        object Success : Result()
        data class Error(val message: String) : Result()
    }

    suspend fun login(login: String, password: String): Result {
        return try {
            val response = authApi.login(LoginRequest(login, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    tokenStorage.saveTokens(body.accessToken, body.refreshToken)
                    Result.Success
                } else {
                    Result.Error("Пустой ответ сервера")
                }
            } else {
                when (response.code()) {
                    401 -> Result.Error("Неверный логин или пароль")
                    in 500..599 -> Result.Error("Ошибка сервера, попробуйте позже")
                    else -> Result.Error("Ошибка: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Result.Error("Нет соединения с сервером")
        }
    }

}