package com.example.vkr.di

import com.example.vkr.logic.navigation.AppNavigator
import com.example.vkr.logic.repositories.CellRepository
import com.example.vkr.presentation.navigation.NavigatorImpl
import com.example.vkr.storage.repositories.CellRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(impl: NavigatorImpl): AppNavigator
}
