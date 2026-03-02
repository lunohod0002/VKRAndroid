package com.example.vkr.isolation

import com.example.vkr.domain.services.AttractionService
import com.example.vkr.domain.services.NotificationService
import com.example.vkr.domain.services.StationService
import com.example.vkr.domain.services.TelephoneService

class StationInfo(station: Any?, attractions: Any?) {

}

class StationFacade(
    private val telephoneService: TelephoneService,
    private val stationService: StationService,
    private val notificationService: NotificationService,
    private val attractionService: AttractionService,
) {
    fun getCurrentStation(): StationInfo? {
        val stationDetails = telephoneService.getCurrentStationNameAndBranch()
        if (stationDetails == null){
            notificationService.sendNotificationNoStation()
            return null
        }
        val stationWithMedias = stationService.getStationWithMedias(
            stationDetails.name,stationDetails.branch)

        val attractions = attractionService.getStationAttractionsWithMedias(
            stationDetails.name,stationDetails.branch)

        return StationInfo(stationWithMedias!!, attractions!!)
    }
    fun getStation(stationDetails: StationDetails): StationInfo? {
        val stationWithMedias = stationService.getStationWithMedias(
            stationDetails.name,stationDetails.branch)

        val attractions = attractionService.getStationAttractionsWithMedias(
            stationDetails.name,stationDetails.branch)

        return StationInfo(stationWithMedias!!, attractions!!)
    }
}