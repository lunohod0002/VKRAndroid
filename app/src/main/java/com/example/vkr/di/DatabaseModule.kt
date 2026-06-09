package com.example.vkr.di

import android.content.Context
import androidx.room.Room
import com.example.vkr.storage.AppDatabase
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.storage.dao.StationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideCellDao(db: AppDatabase): CellDao = db.cellDao()
    @Provides
    fun provideStationlDao(db: AppDatabase): StationDao = db.stationDao()
}