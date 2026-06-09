package com.example.vkr.storage.dao

// storage/dao/StationDao.kt
import com.example.vkr.storage.models.StationEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {
    @Query("SELECT * FROM stations")
    suspend fun getAllStations(): List<StationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<StationEntity>)

    @Query("DELETE FROM stations")
    suspend fun deleteAll()

    @Query("""
    SELECT s.* FROM stations AS s
    INNER JOIN cell AS c ON c.stationId = s.id
    WHERE c.cid = :cid
      AND c.lac = :lac
      AND c.mcc = :mcc
      AND c.mnc = :mnc
      AND c.radio = :radio
    LIMIT 1
""")
    suspend fun getStationByCellTower(
        cid: String,
        lac: String,
        mcc: String,
        mnc: String,
        radio: String
    ): StationEntity?
}