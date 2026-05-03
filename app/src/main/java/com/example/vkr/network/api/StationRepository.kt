package com.example.vkr.network.api

import com.example.vkr.network.dto.Station
import com.example.vkr.network.dto.StationAttractionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StationRepository {
    @GET(value="/api/stations")
    suspend fun getStationByNameAndBranch(@Query("stationName") name:String, @Query("branch") branch:String): Response<Station>
    @GET(value="/stations/{stationId}/attractions")
    suspend fun getStationAttractions(@Path("stationId") stationId:Long): Response<StationAttractionsResponse>

}