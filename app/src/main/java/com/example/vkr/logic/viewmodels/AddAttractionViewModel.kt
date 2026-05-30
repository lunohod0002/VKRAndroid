package com.example.vkr.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.RetrofitClient
import com.example.vkr.network.api.AttractionApi
import com.example.vkr.network.api.AttractionApiImpl
import com.example.vkr.network.dto.AttractionRequest
import com.example.vkr.network.dto.StationAttractionRequest
import kotlinx.coroutines.launch

class AddAttractionViewModel(
    private val repository: AttractionApiImpl
) : ViewModel() {

    sealed interface UiState {
        data object Idle : UiState
        data object Loading : UiState
        data class Success(val id: Long) : UiState
        data class Error(val message: String) : UiState
    }

    private val _state = MutableLiveData<UiState>(UiState.Idle)
    val state: LiveData<UiState> = _state

    private val _stations = MutableLiveData<List<StationAttractionRequest>>(emptyList())
    val stations: LiveData<List<StationAttractionRequest>> = _stations

    val photoUris = mutableListOf<Uri>()
    val videoUris = mutableListOf<Uri>()
    val audioUris = mutableListOf<Uri>()

    fun addStation(item: StationAttractionRequest) {
        _stations.value = (_stations.value ?: emptyList()) + item
    }

    fun removeStation(index: Int) {
        val current = _stations.value.orEmpty().toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _stations.value = current
        }
    }

    fun submit(
        name: String,
        description: String,
        address: String,
        workingHours: String,
        phone: String,
        email: String,
        website: String,
        price: Int? = null
    ) {
        if (_state.value is UiState.Loading) return

        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val photos = repository.uploadMedia(photoUris, "PHOTO")
                val videos = repository.uploadMedia(videoUris, "VIDEO")
                val audios = repository.uploadMedia(audioUris, "AUDIO")
                val medias = photos + videos + audios

                val request = AttractionRequest(
                    name = name,
                    description = description,
                    address = address,
                    price = price,
                    workingHours = workingHours,
                    phoneNumber = phone,
                    email = email,
                    urlRef = website,
                    medias = medias,
                    stationAttractions = _stations.value.orEmpty()
                )

                val response = repository.createAttraction(request)
                _state.value = UiState.Success(response.id)
            } catch (t: Throwable) {
                _state.value = UiState.Error(t.message ?: "Unknown error")
            }
        }
    }

    /**
     * Фабрика лежит прямо внутри ViewModel — чтобы фрагменту не нужно
     * было знать про устройство зависимостей.
     */
    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            val appContext = context.applicationContext
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(AddAttractionViewModel::class.java)) {
                        "Unknown ViewModel class: ${modelClass.name}"
                    }
                    val repository = AttractionApiImpl(
                        context = appContext,
                        mediaApi = RetrofitClient.mediaApi(),
                        attractionApi = RetrofitClient.attractionApi()
                    )
                    return AddAttractionViewModel(repository) as T
                }
            }
        }
    }
}