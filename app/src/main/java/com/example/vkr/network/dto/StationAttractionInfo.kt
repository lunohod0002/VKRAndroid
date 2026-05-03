package com.example.vkr.network.dto

data class StationAttractionInfo(
    var id: Long,
    var name: String,
    var price: Int,
    var urlRef: String,
    var distance: Int
) {}