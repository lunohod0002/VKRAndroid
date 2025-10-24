package com.example.vkr

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vkr.dao.CellDao
import com.example.vkr.dao.NotFoundCellDao
import com.example.vkr.models.request.CellEntity
import com.example.vkr.models.request.NotFoundCell
import com.example.vkr.models.request.StationEntity

@Database(
    entities = [CellEntity::class, StationEntity::class, NotFoundCell::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao(): CellDao
    abstract fun notFoundCellDao(): NotFoundCellDao

}