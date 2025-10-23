package com.example.vkr

import com.example.vkr.dao.CellDao
import com.example.vkr.models.request.CellInfo

class CellRepository(private val cellDao: CellDao) {

    val allLogs = cellDao.getAllLogs() // Flow<List<LogEntry>>

    // Опционально: если хотите создавать объект "на лету"
    suspend fun insert(
        lac: String?,
        mcc: String?,
        mnc: String?,
        cid: String?,
        radio: String?
    ) {
        val cell = CellInfo(
            lac = lac,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            radio = radio
        )
        cellDao.insert(cell)
    }
}
}

// Где-то в Activity/Fragment:
