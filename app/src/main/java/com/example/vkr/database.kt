package com.example.vkr

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vkr.dao.CellDao
import com.example.vkr.dao.CellUpdateDao
import com.example.vkr.models.CellEntity
import com.example.vkr.models.CellEntityUpdate


@Database(
    entities = [CellEntity::class, CellEntityUpdate::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao(): CellDao
    abstract fun notFoundCellDao(): CellUpdateDao

}