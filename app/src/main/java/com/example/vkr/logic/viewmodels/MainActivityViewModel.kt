package com.example.vkr.logic.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vkr.storage.models.CellTower
import com.example.vkr.logic.repositories.CellRepository
import com.example.vkr.logic.repositories.TelephoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val cellRepository: CellRepository,
    private val telephoneRepository: TelephoneRepository,
) : ViewModel() {

    fun fetchCurrentLocation(): CellTower? {
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
//    companion object {
//        fun Factory(context: Context,cellDao: CellDao): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//            ): T {
//                val telephoneRepository = TelephoneRepositoryImpl(context = context)
//                val cellRepository = CellRepositoryImpl(cellDao)
//                return MainActivityViewModel(
//                    cellRepository,
//                    telephoneRepository
//                ) as T
//            }
//        }
//    }
}