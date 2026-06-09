package com.example.vkr.logic.repositories

import com.example.vkr.logic.models.Attraction
import com.example.vkr.logic.models.Station
import com.example.vkr.logic.models.StationAttractionsResponse

interface StationAPIRepository {
    suspend fun getStationByNameAndBranch(name: String, branch: String): Station?
    suspend fun getStationAttractions(stationId: Long): StationAttractionsResponse?
    suspend fun getAttraction(attractionId: Long): Attraction?
}