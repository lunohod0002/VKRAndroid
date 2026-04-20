package com.example.vkr.storage.dao

import androidx.room.*
import com.example.vkr.storage.models.CellTower
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cellTower: CellTower): Long

    @Insert
    suspend fun insertAll(cellTowers: List<CellTower>)

    @Query("SELECT * FROM cell ORDER BY id DESC")
    fun getAllCells(): List<CellTower>

    @Query("SELECT * FROM cell WHERE id = :id")
    suspend fun getCellById(id: Long): CellTower?

    @Query("SELECT * FROM cell WHERE cid = :cid and lac = :lac and mcc = :mcc and mnc = :mnc and radio = :radio")
    fun getCellByAllInfo(cid: String,lac: String,mcc:String,mnc:String,radio:String): CellTower?

    @Query("SELECT * FROM cell WHERE station = :station and branch =:branch")
    fun getCellsByStationNameAndBranch(station: String,branch: Int): Flow<List<CellTower>>

    @Delete
    suspend fun delete(cellTower: CellTower)

    @Query("DELETE FROM cell")
    suspend fun deleteAll()
}