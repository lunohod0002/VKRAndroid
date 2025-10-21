package com.example.vkr
import com.example.vkr.models.request.CellInfo
import com.example.vkr.models.response.CellLocation
import com.example.vkr.UnwiredLabsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OpenCellRepository(
    private val service: UnwiredLabsService
) {
    suspend fun getLocationByCellInfo(cellInfo: CellInfo) = flow {
        val response = service.getLocationByCellInfo(cellInfo)
        emit(response.body())
    }.flowOn(Dispatchers.IO)
}
