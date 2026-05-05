package com.example.vkr.logic.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.api.StationRepository
import com.example.vkr.network.api.StationRepositoryImpl
import com.example.vkr.network.dto.Attraction
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

class AttractionViewModel(
    private val stationRepository: StationRepository,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<Attraction?>()
    val resultLive: LiveData<Attraction?> = resultLiveMutable


    fun getAttraction(stationId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
//            val attractions = listOf(
//                StationAttractionInfo(1, "Зоопарк", 390, "https://s0.rbk.ru/v6_top_pics/media/img/1/14/756594550679141.webp",1500),
//                StationAttractionInfo(2, "Музей космонавтики", 850, "https://cdn.iz.ru/sites/default/files/news-2018-12/2880px-Colosseum_in_Rome%2C_Italy_-_April_2007.jpg",800),
//                StationAttractionInfo(3, "Парк Горького", 210, "https://safety-rest.ru/upload/iblock/655/655861e57c7196758fe81b8c0f19a436.jpg",0),
//                StationAttractionInfo(4, "Третьяковская галерея", 2100,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwyIS3lgTauIU1J_3ECsDxnqF8jyaIlcBQTg&s", 1200),
//                StationAttractionInfo(5, "Бащня", 2100,"https://depositphotos-blog.s3.eu-west-1.amazonaws.com/uploads/2017/07/Depositphotos_5593372_m-2015.jpg", 500)
//
//            )
//            resultLiveMutable.postValue(attractions)

            val attraction = stationRepository.getAttraction(stationId)
            resultLiveMutable.postValue(attraction.body())

            }
        }


    companion object {
        fun Factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {
                val stationRepository = StationRepositoryImpl()
                return AttractionViewModel(stationRepository
                ) as T
            }
        }
    }
}