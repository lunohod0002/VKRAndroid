package com.example.vkr.network.api

import com.example.vkr.logic.models.Station
import android.util.Log
import com.example.vkr.logic.models.Attraction
import com.example.vkr.logic.models.StationAttractionsResponse
import com.example.vkr.logic.repositories.StationAPIRepository
import javax.inject.Inject

class StationAPIRepositoryImpl @Inject constructor(
    private val stationApi: StationApi
) : StationAPIRepository {

    override suspend fun getStationByNameAndBranch(
        name: String,
        branch: String
    ): Station? {
        val response = stationApi.getStationByNameAndBranch(name = name, branch = branch)
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            body
        } else {
            Log.e("Error", response.toString())
            null
        }
    }

    override suspend fun getStationAttractions(stationId: Long): StationAttractionsResponse? {
        val response = stationApi.getStationAttractions(stationId)
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            body
        } else {
            Log.e("Error", response.toString())
            null
        }
    }

    override suspend fun getAttraction(attractionId: Long): Attraction? {
        val response = stationApi.getAttraction(attractionId)
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            body
        } else {
            Log.e("Error", response.toString())
            null
        }
    }
}