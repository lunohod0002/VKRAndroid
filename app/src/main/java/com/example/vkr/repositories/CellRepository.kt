package com.example.vkr.repositories

import com.example.vkr.dao.CellDao
import com.example.vkr.models.CellEntity
import kotlinx.coroutines.flow.Flow

class CellRepository(private val cellDao: CellDao) {

    val allCells: List<CellEntity> = cellDao.getAllCells()
    fun getCellAllInfo(
        lac: String,
        mcc: String,
        mnc: String,
        cid: String,
        radio: String
    ) : CellEntity?{

        return cellDao.getCellByAllInfo(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            radio = radio)
    }


    suspend fun insert(
        lac: String?,
        mcc: String?,
        mnc: String?,
        cid: String?,
        radio: String?
    ) {
        val cell = CellEntity(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            radio = radio,
        )

        cellDao.insert(cell)
    }
}