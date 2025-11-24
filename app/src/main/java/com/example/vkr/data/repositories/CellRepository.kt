package com.example.vkr.data.repositories

import com.example.vkr.data.dao.CellDao
import com.example.vkr.domain.repositories.CellRepository
import com.example.vkr.domain.models.CellEntity

class CellRepositoryImpl(private val cellDao: CellDao): CellRepository {
    override fun getAllCells(): List<CellEntity> {
        return cellDao.getAllCells()

    }

    override fun getCellAllInfo(
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


    override suspend fun insert(
        lac: String?,
        mcc: String?,
        mnc: String?,
        cid: String?,
        radio: String?,
        station: String?,

        ) {
        val cell = CellEntity(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            station = station,
            radio = radio,
        )

        cellDao.insert(cell)
    }
}