package com.example.vkr.models

import com.example.vkr.models.response.CellLocation

sealed class State {
    object Loading : State()
    data class Success(val response: CellLocation) : State()
    data class Failed(val message: String) : State()
}
