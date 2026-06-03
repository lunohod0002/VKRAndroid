package com.example.vkr.logic.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.RetrofitClient
import com.example.vkr.network.api.StationRepositoryImpl
import com.example.vkr.logic.models.Attraction
import com.example.vkr.logic.navigation.AppNavigator
import com.example.vkr.logic.navigation.NavigationCommand
import com.example.vkr.logic.repositories.StationRepository
import com.example.vkr.network.dto.MockAttractions
import com.example.vkr.presentation.fragments.AttractionFragmentDetails
import com.example.vkr.presentation.fragments.AttractionFragmentDetailsDirections
import com.example.vkr.presentation.fragments.StationFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@HiltViewModel
class AttractionViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<Attraction?>()
    val resultLive: LiveData<Attraction?> = resultLiveMutable


    fun getAttraction(stationId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val attraction = MockAttractions.first()
            resultLiveMutable.postValue(attraction)

//            val attraction = stationRepository.getAttraction(stationId)
//            resultLiveMutable.postValue(attraction)

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