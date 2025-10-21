package com.example.vkr

import com.example.vkr.models.request.CellInfo
import com.example.vkr.models.response.CellLocation

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class CellLocationService : UnwiredLabsService {


    private val networkApi: UnwiredLabsService by lazy {
        Retrofit.Builder()
            .baseUrl("http://217.26.26.131/")

            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnwiredLabsService::class.java)
    }

    override suspend fun getLocationByCellInfo(cellInfo: CellInfo): Response<CellLocation> = networkApi.getLocationByCellInfo(cellInfo)

}
