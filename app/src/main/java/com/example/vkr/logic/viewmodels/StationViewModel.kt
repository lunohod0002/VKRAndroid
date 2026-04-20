package com.example.vkr.logic.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.network.dto.Station
import com.example.vkr.network.api.StationRepository
import com.example.vkr.network.api.StationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StationViewModel(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<Station?>()
    val resultLive: LiveData<Station?> = resultLiveMutable

    fun getStationInfo(name:String,branch:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val station = stationRepository.getStationByNameAndBranch(name = name, branch = branch)
            if (station.body() != null) {
                resultLiveMutable.postValue(station.body())
            } else {
                resultLiveMutable.postValue(null)

            }

        }
    }


    companion object {
        fun Factory(context: Context,cellDao: CellDao): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {
                val stationRepository = StationRepositoryImpl()
                return StationViewModel(
                    stationRepository
                ) as T
            }
        }
    }
}