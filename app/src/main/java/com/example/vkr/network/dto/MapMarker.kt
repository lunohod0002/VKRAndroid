package com.example.vkr.network.dto

data class MapMarker(
    val coordinates: StationCoordinates,
    val title: String,
    val branchNumber: Int
)