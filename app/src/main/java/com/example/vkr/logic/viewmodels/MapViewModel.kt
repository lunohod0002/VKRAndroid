package com.example.vkr.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkr.logic.navigation.AppNavigator
import com.example.vkr.logic.navigation.NavigationCommand
import com.example.vkr.logic.repositories.StationRepository
import com.example.vkr.logic.repositories.TelephoneRepository
import com.example.vkr.network.dto.MapMarker
import com.example.vkr.network.dto.StationCoordinates
import com.example.vkr.network.dto.StationData
import com.example.vkr.presentation.fragments.MapFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MapViewModel @Inject constructor(
    private val telephoneRepository: TelephoneRepository,
    private val stationRepository: StationRepository,
    private val navigator: AppNavigator
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<StationCoordinates?>()
    val resultLive: LiveData<StationCoordinates?> = resultLiveMutable

    private val markersMutable = MutableLiveData<List<MapMarker>>()
    val markers: LiveData<List<MapMarker>> = markersMutable

    init { loadStations() }

    private fun loadStations() {
        viewModelScope.launch(Dispatchers.IO) {
            val entities = stationRepository.getStations()
            val mapMarkers = entities.map { entity ->
                MapMarker(
                    coordinates = StationCoordinates(entity.latitude, entity.longitude),
                    title = entity.name,
                    branchNumber = mapBranchToNumber(entity.branch)
                )
            }
            markersMutable.postValue(mapMarkers)
        }
    }

    private fun mapBranchToNumber(branchName: String): Int = when (branchName) {
        "Сокольническая" -> 1
        "Серпуховско-Тимирязевская" -> 9
        "Арбатско-Покровская" -> 3
        "Кольцевая" -> 5
        else -> 0
    }

    fun fetchCurrentLocation() {
        val cellInfo = telephoneRepository.getCurrentCellInfo()
        if (cellInfo == null) {
            resultLiveMutable.value = null
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val station = stationRepository.getStationByCellTower(
                cid = cellInfo.cid!!.toString(),
                lac = cellInfo.lac!!.toString(),
                mcc = cellInfo.mcc!!.toString(),
                mnc = cellInfo.mnc!!.toString(),
                radio = cellInfo.radio!!.toString()
            )

            if (station != null) {
                resultLiveMutable.postValue(
                    StationCoordinates(station.latitude, station.longitude)
                )
            } else {
                resultLiveMutable.postValue(null)
            }
        }
    }

    fun navigateToStation(stationData: StationData) {
        val direction = MapFragmentDirections.actionScreenMapToScreenStation(STATION = stationData)
        navigator.navigate(NavigationCommand.To(direction))
    }
}