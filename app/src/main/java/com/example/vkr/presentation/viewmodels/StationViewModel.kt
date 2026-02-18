package com.example.vkr.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.data.dao.CellDao
import com.example.vkr.data.repositories.CellRepositoryImpl
import com.example.vkr.data.repositories.TelephoneRepositoryImpl
import com.example.vkr.domain.dto.StationCoordinates
import com.example.vkr.domain.models.Station
import com.example.vkr.domain.repositories.StationRepository
import com.example.vkr.presentation.repositories.StationRepositoryImpl
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