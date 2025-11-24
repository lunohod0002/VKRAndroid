package com.example.vkr

import android.app.Application
import androidx.room.Room
import com.example.vkr.data.AppDatabase
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    private lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("f6e4c13c-e050-4c06-a23c-dda1c9db8a29")

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