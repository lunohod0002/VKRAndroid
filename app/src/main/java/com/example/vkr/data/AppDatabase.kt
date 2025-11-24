package com.example.vkr.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkr.data.dao.CellDao
import com.example.vkr.domain.models.CellEntity

@Database(
    entities = [CellEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao(): CellDao

}