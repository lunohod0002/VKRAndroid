package com.example.vkr.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.api.AttractionRepositoryImpl
import com.example.vkr.logic.models.AttractionRequest
import com.example.vkr.network.dto.StationAttractionRequest
import kotlinx.coroutines.launch

class AddAttractionViewModel(
    private val repository: AttractionRepositoryImpl
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


}