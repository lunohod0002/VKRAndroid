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
import com.example.vkr.network.dto.StationAttractionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StationViewModel(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<Station?>()
    val resultLive: LiveData<Station?> = resultLiveMutable

    fun getStationInfo(name:String,branch:String) {

        viewModelScope.launch(Dispatchers.IO) {
            resultLiveMutable.postValue(
                Station(
                    ",", ",",
                    listOf(
                        "1",
                        "2",
                    ),
                    "2",
                    listOf(
                        "https://classpic.ru/wp-content/uploads/2016/02/15222/Hitryj-minon.jpg",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnTQ04WdzI8_nx_D7_gGQK5nyjsunQOHNm5g&s",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnkXX1msb3FcwUKdveOb4VJ_8dlsezqUlqEQ&s",
                    ),
                    emptyList(), emptyList(),listOf(
                        StationAttractionResponse(1,"Московский зоопарк и еее кп цв йцвйцв йцв цц", 200,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-_HGFkK3BhjnV2sHm3zv11GcRlHEjIq4zGg&s","t"),
                        StationAttractionResponse(1,"Зоопарк", 200,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-_HGFkK3BhjnV2sHm3zv11GcRlHEjIq4zGg&s","t")
                    )
                )
            )
//            val station = stationRepository.getStationByNameAndBranch(name = name, branch = branch)
//            if (station.body() != null) {
//                resultLiveMutable.postValue(station.body())
//            } else {
//                resultLiveMutable.postValue(null)
//
//            }

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