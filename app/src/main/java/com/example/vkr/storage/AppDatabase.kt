package com.example.vkr.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.storage.models.CellTower

@Database(
    entities = [CellTower::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao(): CellDao

}