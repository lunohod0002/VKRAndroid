package com.example.vkr

import android.app.Application
import androidx.room.Room

class App : Application() {
    private lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        this.db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,"database"
        )
            .createFromAsset("database.db")
            .fallbackToDestructiveMigrationFrom(dropAllTables = true)
            .build()
    }

    fun getDb(): AppDatabase {
        return db
    }
}