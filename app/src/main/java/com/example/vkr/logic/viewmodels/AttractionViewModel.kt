package com.example.vkr.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkr.logic.models.Attraction
import com.example.vkr.logic.navigation.AppNavigator
import com.example.vkr.logic.navigation.NavigationCommand
import com.example.vkr.logic.repositories.StationAPIRepository
import com.example.vkr.presentation.fragments.AttractionFragmentDetailsDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@HiltViewModel
class AttractionViewModel @Inject constructor(
    private val stationAPIRepository: StationAPIRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<Attraction?>()
    val resultLive: LiveData<Attraction?> = resultLiveMutable


    fun getAttraction(stationId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
//            val attraction = MockAttractions.first()
//            resultLiveMutable.postValue(attraction)

            val attraction = stationAPIRepository.getAttraction(stationId)
            resultLiveMutable.postValue(attraction)

            }
        }
    fun openAudioGuide(
        audioUrl: String,

        ) {
        val direction = AttractionFragmentDetailsDirections
            .actionAttractionFragmentToAudioPlayerBottomSheet(
                audioUrl = audioUrl
            )
        navigator.navigate(NavigationCommand.To(direction))
    }
    fun openVideoGuide(
        videoUrl: String,
    ) {
        val direction = AttractionFragmentDetailsDirections
            .actionAttractionFragmentToVideoPlayerBottomSheet(
                videoUrl = videoUrl
            )

        navigator.navigate(NavigationCommand.To(direction))
    }



}