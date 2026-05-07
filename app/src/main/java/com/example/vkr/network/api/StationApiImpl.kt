package com.example.vkr.network.api

import com.example.vkr.network.dto.Station
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import com.example.vkr.network.dto.Attraction
import com.example.vkr.network.dto.StationAttractionsResponse

class StationApiImpl(
    private val stationApi: StationApi
){

suspend fun getStationByNameAndBranch(
        name: String,
        branch: String
    ): Response<Station> {
    stationApi.getStationByNameAndBranch(name=name,branch=branch).let { response ->
            if (response.code() == 200 && response.body() != null) {
                return response
            } else {
                Log.e("Error", response.toString())
                return response
            }
        }
    }


    suspend fun getStationAttractions(stationId: Long): Response<StationAttractionsResponse> {
        stationApi.getStationAttractions(stationId).let { response ->
            if (response.code() == 200 && response.body() != null) {
                return response
            } else {
                Log.e("Error", response.toString())
                return response
            }
        }
    }

    suspend fun getAttraction(attractionId: Long): Response<Attraction> {
        stationApi.getAttraction(attractionId).let { response ->
            if (response.code() == 200 && response.body() != null) {
                return response
            } else {
                Log.e("Error", response.toString())
                return response
            }
        }
    }
}