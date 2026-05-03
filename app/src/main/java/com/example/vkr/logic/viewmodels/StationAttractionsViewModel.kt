package com.example.vkr.logic.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.api.StationRepository
import com.example.vkr.network.api.StationRepositoryImpl
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.storage.repositories.CellRepositoryImpl
import com.example.vkr.storage.repositories.TelephoneRepositoryImpl
import com.example.vkr.network.dto.MapMarker
import com.example.vkr.network.dto.StationAttractionInfo
import com.example.vkr.network.dto.StationCoordinates
import com.example.vkr.storage.repositories.CellRepository
import com.example.vkr.storage.repositories.TelephoneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StationAttractionsViewModel(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<List<StationAttractionInfo>?>()
    val resultLive: LiveData<List<StationAttractionInfo>?> = resultLiveMutable


    fun getStationAttractions(stationId:Long) {
        viewModelScope.launch(Dispatchers.IO) {

            val attractions = stationRepository.getStationAttractions(stationId)
            resultLiveMutable.postValue(attractions.body()?.content)

            }
        }


    companion object {
        fun Factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {
                val stationRepository = StationRepositoryImpl()
                return StationAttractionsViewModel(stationRepository
                ) as T
            }
        }
    }
}