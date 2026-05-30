package com.example.vkr

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.vkr.storage.AppDatabase
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("f6e4c13c-e050-4c06-a23c-dda1c9db8a29")

    }

}