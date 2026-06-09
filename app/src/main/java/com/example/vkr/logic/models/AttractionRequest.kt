package com.example.vkr.logic.models

import com.example.vkr.network.dto.StationAttractionRequest

data class AttractionRequest(
    val name: String,
    val description: String,
    val address: String,
    val price: Int?,
    val workingHours: String,
    val phoneNumber: String,
    val email: String,
    val urlRef: String,
    val medias: List<MediaRequest>,
    val stationAttractions: List<StationAttractionRequest>
)
