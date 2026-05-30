package com.example.vkr.di

import com.example.vkr.logic.repositories.CellRepository
import com.example.vkr.logic.repositories.TelephoneRepository
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
}