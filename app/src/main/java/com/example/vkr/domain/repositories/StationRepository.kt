package com.example.vkr.domain.repositories

import com.example.vkr.domain.models.Station
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StationRepository {
    @GET(value="/api/stations/")
    suspend fun getStationByNameAndBranch(@Query("stationName") name:String, @Query("branch") branch:String): Response<Station>
}