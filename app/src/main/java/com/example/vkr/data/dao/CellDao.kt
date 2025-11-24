package com.example.vkr.data.dao

import androidx.room.*
import com.example.vkr.domain.models.CellEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cell: CellEntity): Long

    @Insert
    suspend fun insertAll(cells: List<CellEntity>)

    @Query("SELECT * FROM cell ORDER BY id DESC")
    fun getAllCells(): List<CellEntity>

    @Query("SELECT * FROM cell WHERE id = :id")
    suspend fun getCellById(id: Long): CellEntity?

    @Query("SELECT * FROM cell WHERE cid = :cid and lac = :lac and mcc = :mcc and mnc = :mnc and radio = :radio")
    fun getCellByAllInfo(cid: String,lac: String,mcc:String,mnc:String,radio:String): CellEntity?

    @Query("SELECT * FROM cell WHERE station = :station and branch =:branch")
    fun getCellsByStationNameAndBranch(station: String,branch: Int): Flow<List<CellEntity>>

    @Delete
    suspend fun delete(cell: CellEntity)

    @Query("DELETE FROM cell")
    suspend fun deleteAll()
}