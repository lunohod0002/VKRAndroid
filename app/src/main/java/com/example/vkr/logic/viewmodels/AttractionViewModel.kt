package com.example.vkr.logic.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.RetrofitClient
import com.example.vkr.network.api.StationApi
import com.example.vkr.network.api.StationApiImpl
import com.example.vkr.network.dto.Attraction
import com.example.vkr.network.dto.MockAttractions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AttractionViewModel(
    private val stationApi: StationApiImpl,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<Attraction?>()
    val resultLive: LiveData<Attraction?> = resultLiveMutable


    fun getAttraction(stationId:Long) {
        viewModelScope.launch(Dispatchers.IO) {
//            val attraction = MockAttractions.first()
//            resultLiveMutable.postValue(attraction)

            val attraction = stationApi.getAttraction(stationId)
            resultLiveMutable.postValue(attraction.body())

            }
        }


    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {
                val stationApi = StationApiImpl(
                    RetrofitClient.stationApi()
                )
                return AttractionViewModel(stationApi
                ) as T
            }
        }
    }
}