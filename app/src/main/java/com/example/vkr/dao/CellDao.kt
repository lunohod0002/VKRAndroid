package com.example.vkr.dao

import androidx.room.*
import com.example.vkr.models.request.CellInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(logEntry: CellInfo): Long

    @Insert
    suspend fun insertAll(entries: List<CellInfo>)

    @Query("SELECT * FROM cellInfo ORDER BY id DESC")
    fun getAllLogs(): Flow<List<CellInfo>>

    @Query("SELECT * FROM cellInfo WHERE id = :id")
    suspend fun getLogById(id: Long): CellInfo?

    @Delete
    suspend fun delete(logEntry: CellInfo)

    @Query("DELETE FROM cellInfo")
    suspend fun deleteAll()
}