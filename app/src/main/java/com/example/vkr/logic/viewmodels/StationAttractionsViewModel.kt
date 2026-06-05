package com.example.vkr.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkr.logic.models.StationAttractionInfo
import com.example.vkr.logic.navigation.AppNavigator
import com.example.vkr.logic.navigation.NavigationCommand
import com.example.vkr.logic.repositories.StationRepository
import com.example.vkr.network.dto.AttractionId
import com.example.vkr.presentation.fragments.StationAttractionsFragmentDirections
import com.example.vkr.presentation.fragments.StationFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationAttractionsViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val navigator: AppNavigator
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<List<StationAttractionInfo>?>()
    val resultLive: LiveData<List<StationAttractionInfo>?> = resultLiveMutable
    fun navigateToAttractionDetails(attractionId: Long){
        val direction =
            StationAttractionsFragmentDirections.actionStationAttractionsFragmentToAttractionFragmentDetails(
                ATTRACTION = AttractionId(attractionId)
            )
        navigator.navigate(NavigationCommand.To(direction))
    }

    fun getStationAttractions(stationId:Long) {
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

            val attractions = stationRepository.getStationAttractions(stationId)
            resultLiveMutable.postValue(attractions?.content)

            }
        }



}