package com.example.vkr.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.storage.dao.StationDao
import com.example.vkr.storage.models.CellTower
import com.example.vkr.storage.models.StationEntity

@Database(
    entities = [CellTower::class, StationEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao(): CellDao
    abstract fun stationDao(): StationDao

}