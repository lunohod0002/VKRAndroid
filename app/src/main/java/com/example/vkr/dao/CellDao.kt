package com.example.vkr.dao

import androidx.room.*
import com.example.vkr.models.request.CellEntity
import com.example.vkr.models.request.CellInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cell: CellEntity): Long

    @Insert
    suspend fun insertAll(cells: List<CellEntity>)

    @Query("SELECT * FROM cellInfo ORDER BY id DESC")
    fun getAllCells(): List<CellEntity>

    @Query("SELECT * FROM cellInfo WHERE id = :id")
    suspend fun getCellById(id: Long): CellEntity?

    @Query("SELECT * FROM cellInfo WHERE stationId = :stationId")
    fun getCellsByStationId(stationId: Long): Flow<List<CellEntity>>

    @Delete
    suspend fun delete(cell: CellEntity)

    @Query("DELETE FROM cellInfo")
    suspend fun deleteAll()
}