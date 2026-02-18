package com.example.vkr.data.repositories

import com.example.vkr.data.dao.CellDao
import com.example.vkr.domain.repositories.CellRepository
import com.example.vkr.domain.models.CellTower

class CellRepositoryImpl(private val cellDao: CellDao): CellRepository {
    override fun getAllCells(): List<CellTower> {
        return cellDao.getAllCells()

    }

    override fun getCellAllInfo(
        lac: String,
        mcc: String,
        mnc: String,
        cid: String,
        radio: String
    ) : CellTower?{

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
        station: String?,
        radio: String?,

        ) {
        val cellTower = CellTower(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            station = station,
            radio = radio,
        )

        cellDao.insert(cellTower)
    }
}