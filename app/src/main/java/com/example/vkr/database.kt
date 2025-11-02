package com.example.vkr

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkr.dao.CellDao
import com.example.vkr.models.CellEntity


@Database(
    entities = [CellEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao(): CellDao

}