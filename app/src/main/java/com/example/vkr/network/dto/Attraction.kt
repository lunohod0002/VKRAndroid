package com.example.vkr.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class Attraction(
    var name: String,
    var phoneNumber: String,
    var email: String,
    var address: String,
    val images: List<String>,
    var description: String,
    var workingHours: String,
    var price: String?,
    val audioUrl: String?,
    val videoUrl: String?,
    var urlRef: String
)