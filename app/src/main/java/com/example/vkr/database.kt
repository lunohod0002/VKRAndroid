package com.example.vkr
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vkr.dao.CellDao
import com.example.vkr.models.request.CellInfo

import kotlin.jvm.Volatile;

@Database(
        entities = [CellInfo::class],
version = 1,
exportSchema = false
        )
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao(): CellDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                )
                // .fallbackToDestructiveMigration() // раскомментируйте при смене версии и отсутствии миграции
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}