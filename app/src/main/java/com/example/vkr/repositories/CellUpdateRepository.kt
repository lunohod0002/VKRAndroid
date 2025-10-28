package com.example.vkr.repositories

import com.example.vkr.dao.CellUpdateDao
import com.example.vkr.models.CellEntityUpdate

class CellUpdateRepository(private val cellDao: CellUpdateDao) {

    val allStations = cellDao.getAllCells()

    suspend fun insert(
        lac: String?,
        mcc: String?,
        mnc: String?,
        cid: String?,
        radio: String?,

    ): Long  {
        val cell = CellEntityUpdate(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            radio = radio
        )
        return cellDao.insert(cell)
    }


}