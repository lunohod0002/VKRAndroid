package com.example.vkr.network.dto

data class StationAttractionRequest(
    val stationName: String,
    val branch: String,
    val distance: Int
)
