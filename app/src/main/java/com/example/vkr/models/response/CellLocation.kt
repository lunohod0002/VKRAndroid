package com.example.vkr.models.response

import com.squareup.moshi.Json
import java.io.Serializable

data class CellLocation(
    val lat: Double? = null,
    val lon: Double? = null
) : Serializable {
}
