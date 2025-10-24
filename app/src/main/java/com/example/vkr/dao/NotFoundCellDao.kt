package com.example.vkr.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vkr.models.request.CellEntity
import com.example.vkr.models.request.NotFoundCell
import kotlinx.coroutines.flow.Flow


@Dao
interface NotFoundCellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cell: NotFoundCell): Long

    @Insert
    suspend fun insertAll(cells: List<NotFoundCell>)

    @Query("SELECT * FROM notFoundCell ORDER BY id DESC")
    fun getAllCells(): List<NotFoundCell>


    @Query("SELECT * FROM notFoundCell WHERE id = :id")
    suspend fun getCellById(id: Long): NotFoundCell?

    @Delete
    suspend fun delete(cell: NotFoundCell)

    @Query("DELETE FROM notFoundCell")
    suspend fun deleteAll()
}