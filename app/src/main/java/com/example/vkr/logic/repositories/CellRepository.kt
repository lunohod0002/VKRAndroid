package com.example.vkr.logic.repositories

import com.example.vkr.storage.models.CellTower

interface CellRepository {
    fun getAllCells() : List<CellTower>
    fun getCellAllInfo(        lac: String,
                               mcc: String,
                               mnc: String,
                               cid: String,
                               radio: String): CellTower?
    suspend fun insert(  lac: String?,
                 mcc: String?,
                 mnc: String?,
                 cid: String?,
                 station: String? = null,
                 radio: String?)
}