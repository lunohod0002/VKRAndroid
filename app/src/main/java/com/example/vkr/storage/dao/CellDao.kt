package com.example.vkr.storage.dao

import androidx.room.*
import com.example.vkr.storage.models.CellTower
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cellTower: CellTower): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cellTowers: List<CellTower>)

    // Остальные методы оставь как есть, но getCellByAllInfo теперь будет возвращать вышку с stationId
    @Query("SELECT * FROM cell WHERE cid = :cid AND lac = :lac AND mcc = :mcc AND mnc = :mnc AND radio = :radio LIMIT 1")
    suspend fun getCellByAllInfo(cid: String, lac: String, mcc: String, mnc: String, radio: String): CellTower?


    @Delete
    suspend fun delete(cellTower: CellTower)

    @Query("DELETE FROM cell")
    suspend fun deleteAll()
}