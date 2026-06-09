package com.example.vkr.storage.models// storage/models/StationEntity.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val branch: String,
    val latitude: Double,
    val longitude: Double,
    val cachedAt: Long = System.currentTimeMillis()
)