package com.example.vkr.logic.repositories

import com.example.vkr.storage.models.CellTower

interface CellRepository {
    suspend fun getCellAllInfo(        lac: String,
                               mcc: String,
                               mnc: String,
                               cid: String,
                               radio: String): CellTower?
}