package com.example.vkr.di

import com.example.vkr.storage.repositories.StationRepositoryImpl
import com.example.vkr.logic.repositories.AttractionRepository
import com.example.vkr.logic.repositories.CellRepository
import com.example.vkr.logic.repositories.StationAPIRepository
import com.example.vkr.logic.repositories.StationRepository
import com.example.vkr.logic.repositories.TelephoneRepository
import com.example.vkr.network.api.AttractionRepositoryImpl
import com.example.vkr.network.api.StationAPIRepositoryImpl
import com.example.vkr.storage.repositories.CellRepositoryImpl
import com.example.vkr.storage.repositories.TelephoneRepositoryImpl   // ← проверь пакет своего Impl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCellRepository(impl: CellRepositoryImpl): CellRepository

    @Binds
    @Singleton
    abstract fun bindTelephoneRepository(impl: TelephoneRepositoryImpl): TelephoneRepository


    @Binds
    @Singleton
    abstract fun bindStationAPIRepository(impl: StationAPIRepositoryImpl): StationAPIRepository

    @Binds
    @Singleton
    abstract fun bindAttractionRepository(impl: AttractionRepositoryImpl): AttractionRepository
    @Binds
    @Singleton
    abstract fun bindStationRepository(impl: StationRepositoryImpl): StationRepository
}