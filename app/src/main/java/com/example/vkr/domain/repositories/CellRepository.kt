package com.example.vkr.domain.repositories

import com.example.vkr.data.models.CellEntity

interface CellRepository {
    fun getAllCells() : List<CellEntity>
    fun getCellAllInfo(        lac: String,
                               mcc: String,
                               mnc: String,
                               cid: String,
                               radio: String): CellEntity?
    suspend fun insert(  lac: String?,
                 mcc: String?,
                 mnc: String?,
                 cid: String?,
                 station: String? = null,
                 radio: String?)
}