package com.example.vkr.storage.repositories

import com.example.vkr.logic.repositories.CellRepository
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.storage.models.CellTower
import jakarta.inject.Inject

class CellRepositoryImpl @Inject constructor(private val cellDao: CellDao): CellRepository {



    override suspend fun getCellAllInfo(
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




}