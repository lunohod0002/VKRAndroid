package com.example.vkr.repositories

import com.example.vkr.dao.NotFoundCellDao
import com.example.vkr.models.request.NotFoundCell

class CellStationRepository(private val cellDao: NotFoundCellDao) {

    val allStations = cellDao.getAllCells()

    suspend fun insert(
        lac: String?,
        mcc: String?,
        mnc: String?,
        cid: String?,
        radio: String?,
        status:String?
    ): Long  {
        val cell = NotFoundCell(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            status = status,
            radio = radio
        )
        return cellDao.insert(cell)
    }
    suspend fun getAll(
        lac: String?,
        mcc: String?,
        mnc: String?,
        cid: String?,
        radio: String?,
        status:String?
    ): Long  {
        val cell = NotFoundCell(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            status = status,
            radio = radio
        )
        return cellDao.insert(cell)
    }
}