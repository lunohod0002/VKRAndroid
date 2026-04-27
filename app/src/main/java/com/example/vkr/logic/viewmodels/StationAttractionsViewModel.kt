package com.example.vkr.logic.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.storage.repositories.CellRepositoryImpl
import com.example.vkr.storage.repositories.TelephoneRepositoryImpl
import com.example.vkr.network.dto.MapMarker
import com.example.vkr.network.dto.StationCoordinates
import com.example.vkr.storage.repositories.CellRepository
import com.example.vkr.storage.repositories.TelephoneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StationAttractionsViewModel(
    private val cellRepository: CellRepository,
    private val telephoneRepository: TelephoneRepository,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<StationCoordinates?>()
    val resultLive: LiveData<StationCoordinates?> = resultLiveMutable


    fun getStationAttractions() {
        //

    }
    companion object {
        fun Factory(context: Context,cellDao: CellDao): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {
                val telephoneRepository = TelephoneRepositoryImpl(context = context)
                val cellRepository = CellRepositoryImpl(cellDao)
                return StationAttractionsViewModel(
                    cellRepository,
                    telephoneRepository
                ) as T
            }
        }
    }
}