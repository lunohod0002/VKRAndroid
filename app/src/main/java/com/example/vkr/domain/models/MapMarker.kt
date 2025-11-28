package com.example.vkr.domain.models

data class MapMarker(
    val coordinates: StationCoordinates,
    val title: String,
    val branchNumber: Int
)
