package com.example.vkr.logic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.RetrofitClient
import com.example.vkr.network.api.AuthApi
import com.example.vkr.network.api.AuthApiImpl
import com.example.vkr.storage.TokenStorage
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val tokenStorage = TokenStorage(app.applicationContext)
    private val repo = AuthApiImpl(RetrofitClient.authApi(tokenStorage), tokenStorage)

    sealed class State {
        object Success : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            _state.value = State.Error("Заполните логин и пароль")
            return
        }
        viewModelScope.launch {
            _state.value = when (val r = repo.login(login.trim(), password)) {
                is AuthApiImpl.Result.Success -> State.Success
                is AuthApiImpl.Result.Error -> State.Error(r.message)
            }
        }
    }
}