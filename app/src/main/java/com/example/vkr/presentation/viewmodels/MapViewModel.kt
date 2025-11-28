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
import com.example.vkr.domain.data.MapMarker
import com.example.vkr.domain.data.StationCoordinates
import com.example.vkr.domain.repositories.CellRepository
import com.example.vkr.domain.repositories.TelephoneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val cellRepository: CellRepository,
    private val telephoneRepository: TelephoneRepository,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<StationCoordinates?>()
    val resultLive: LiveData<StationCoordinates?> = resultLiveMutable

    val markers: List<MapMarker> = listOf(
        MapMarker(StationCoordinates(55.736137, 37.594925), "Парк Культуры", 1),
        MapMarker(StationCoordinates(55.745073, 37.603488), "Кропоткинская", 1),
        MapMarker(
            StationCoordinates(55.751344, 37.610225),
            "Библиотека им. Ленина",
            1
        ),
        MapMarker(StationCoordinates(55.756969, 37.615526), "Охотный ряд", 1),
        MapMarker(StationCoordinates(55.759617, 37.626763), "Лубянка", 1),
        MapMarker(StationCoordinates(55.764796, 37.638691), "Чистые Пруды", 1),
        MapMarker(StationCoordinates(55.768952, 37.648969), "Красные Ворота", 1),
        MapMarker(StationCoordinates(55.774367, 37.653878), "Комсомольская", 1),
        MapMarker(StationCoordinates(55.727293, 37.625098), "Серпуховская", 9),
        MapMarker(StationCoordinates(55.736903, 37.618697), "Полянка", 9),
        MapMarker(StationCoordinates(55.750574, 37.608649), "Боровицкая", 9),
        MapMarker(StationCoordinates(55.765845, 37.608171), "Чеховская", 9),
        MapMarker(StationCoordinates(55.771677, 37.620597), "Цветной Бульвар", 9),
        MapMarker(StationCoordinates(55.781821, 37.598696), "Менделеевская", 9),
        MapMarker(StationCoordinates(55.744701, 37.566815), "Киевская", 9),
        MapMarker(StationCoordinates(55.747751, 37.583834), "Смоленская", 9),
        MapMarker(StationCoordinates(55.752536, 37.604196), "Арбатская", 3),
        MapMarker(StationCoordinates(55.756806, 37.622727), "Площадь Революции", 3),
        MapMarker(StationCoordinates(55.758467, 37.658763), "Курская", 3),
        MapMarker(StationCoordinates(55.760214, 37.577219), "Краснопресненская", 5),
        MapMarker(StationCoordinates(55.779566, 37.601422), "Новослободская", 5),
        MapMarker(StationCoordinates(55.779610, 37.633298), "Краснопресненская", 5),
        MapMarker(StationCoordinates(55.760214, 37.577219), "Проспект Мира", 5),
        MapMarker(StationCoordinates(55.774397, 37.655634), "Комсомольская", 5),
        MapMarker(StationCoordinates(55.776776, 37.585257), "Белоруская", 5),
        MapMarker(StationCoordinates(55.757450, 37.659938), "Курская", 5),
        MapMarker(StationCoordinates(55.742313, 37.653094), "Таганская", 5),
        MapMarker(StationCoordinates(55.731820, 37.637005), "Павелецкая", 5),
        MapMarker(StationCoordinates(55.728995, 37.622745), "Добрынинская", 5),
        MapMarker(StationCoordinates(55.729218, 37.611187), "Киевская", 5),
        MapMarker(StationCoordinates(55.735305, 37.592872), "Парк Культуры", 5),


        )

    fun fetchCurrentLocation() {
        val cellInfo  = telephoneRepository.getCurrentCellInfo()
        if (cellInfo == null){
            resultLiveMutable.value = null
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val cell = cellRepository.getCellAllInfo(
                lac = cellInfo.lac!!,
                mcc = cellInfo.mcc!!,
                mnc = cellInfo.mnc!!,
                cid = cellInfo.cid!!,
                radio = cellInfo.radio!!
            )
            if (cell != null) {
                markers.forEach { it ->
                    if (it.title == cell.station && it.branchNumber == cell.branch) {
                        resultLiveMutable.postValue(it.coordinates)
                    }
                }
                } else{
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
                val telephoneRepository = TelephoneRepositoryImpl(context = context)
                val cellRepository = CellRepositoryImpl(cellDao)
                return MapViewModel(
                    cellRepository,
                    telephoneRepository
                ) as T
            }
        }
    }
}