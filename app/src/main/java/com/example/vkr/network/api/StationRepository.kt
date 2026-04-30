package com.example.vkr.network.api

import com.example.vkr.network.dto.Station
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StationRepository {
    @GET(value="/api/stations")
    suspend fun getStationByNameAndBranch(@Query("stationName") name:String, @Query("branch") branch:String): Response<Station>
}