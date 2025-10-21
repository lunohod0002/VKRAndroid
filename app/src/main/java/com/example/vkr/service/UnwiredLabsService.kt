package com.example.vkr


import com.example.vkr.models.request.CellInfo
import com.example.vkr.models.response.CellLocation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UnwiredLabsService {

    @POST("tower")
    suspend fun getLocationByCellInfo(@Body cellInfo: CellInfo): Response<CellLocation>
}