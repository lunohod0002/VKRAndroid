package com.example.vkr.logic.models

import java.io.Serializable

data class Attraction(
    var name: String,
    var phoneNumber: String?,
    var email: String?,
    var address: String,
    val images: List<String>,
    var description: String,
    var workingHours: String?,
    var price: Int?,
    val audios: List<String>?,
    val videos: List<String>?,
    var url: String?
) : Serializable {}