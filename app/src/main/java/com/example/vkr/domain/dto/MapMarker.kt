package com.example.vkr.domain.dto

data class MapMarker(
    val coordinates: StationCoordinates,
    val title: String,
    val branchNumber: Int
)