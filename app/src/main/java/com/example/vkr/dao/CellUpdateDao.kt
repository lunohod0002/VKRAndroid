package com.example.vkr.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vkr.models.CellEntityUpdate


@Dao
interface CellUpdateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cell: CellEntityUpdate): Long

    @Insert
    suspend fun insertAll(cells: List<CellEntityUpdate>)

    @Query("SELECT * FROM cellUpdate ORDER BY id DESC LIMIT 1")
    fun getAllCells(): List<CellEntityUpdate>


    @Query("SELECT * FROM cellUpdate WHERE id = :id")
    suspend fun getCellById(id: Long): CellEntityUpdate?

    @Delete
    suspend fun delete(cell: CellEntityUpdate)

    @Query("DELETE FROM cellUpdate")
    suspend fun deleteAll()
}