package com.example.vkr.logic.repositories

import com.example.vkr.storage.models.StationEntity

interface StationRepository {
    suspend fun getStations(): List<StationEntity>

    suspend fun getStationByCellTower(
        cid: String, lac: String, mcc: String, mnc: String, radio: String
    ): StationEntity?

}