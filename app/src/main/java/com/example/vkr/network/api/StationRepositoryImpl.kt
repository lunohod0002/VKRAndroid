package com.example.vkr.network.api

import com.example.vkr.network.dto.Station
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

class StationRepositoryImpl : StationRepository {

    companion object {
        const val BASE_URL = "http://192.168.1.20:8081"
    }

    private val networkApi: StationRepository by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StationRepository::class.java)
    }

    override suspend fun getStationByNameAndBranch(
        name: String,
        branch: String
    ): Response<Station> {
        networkApi.getStationByNameAndBranch(name=name,branch=branch).let { response ->
            if (response.code() == 200 && response.body() != null) {
                return response
            } else {
                Log.e("Error", response.toString())
                return response
            }
        }
    }

}