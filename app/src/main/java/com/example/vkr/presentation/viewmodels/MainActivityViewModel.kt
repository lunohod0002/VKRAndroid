package com.example.vkr.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.data.dao.CellDao
import com.example.vkr.data.models.CellEntity
import com.example.vkr.data.repositories.CellRepositoryImpl
import com.example.vkr.data.repositories.TelephoneRepositoryImpl
import com.example.vkr.domain.models.MapMarker
import com.example.vkr.domain.models.StationCoordinates
import com.example.vkr.domain.models.StationData
import com.example.vkr.domain.repositories.CellRepository
import com.example.vkr.domain.repositories.TelephoneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val cellRepository: CellRepository,
    private val telephoneRepository: TelephoneRepository,
) : ViewModel() {

    fun fetchCurrentLocation(): CellEntity? {
        val cellInfo  = telephoneRepository.getCurrentCellInfo()
        if (cellInfo == null){
            return null
        }

            val cell = cellRepository.getCellAllInfo(
                lac = cellInfo.lac!!,
                mcc = cellInfo.mcc!!,
                mnc = cellInfo.mnc!!,
                cid = cellInfo.cid!!,
                radio = cellInfo.radio!!
            )
            return cell

    }
    companion object {
        fun Factory(context: Context,cellDao: CellDao): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {
                val telephoneRepository = TelephoneRepositoryImpl(context = context)
                val cellRepository = CellRepositoryImpl(cellDao)
                return MainActivityViewModel(
                    cellRepository,
                    telephoneRepository
                ) as T
            }
        }
    }
}