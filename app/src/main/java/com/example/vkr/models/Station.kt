package com.example.vkr.models

import java.io.Serializable

data class Station(
    val name: String, val latitude: Double, val longitude:Double
) : Serializable {}
