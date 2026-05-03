package com.example.vkr.network.dto

import java.io.Serializable

data class StationAttractionData(
    val id : Long,
    val title: String,
    val branchNumber: Int

): Serializable {}