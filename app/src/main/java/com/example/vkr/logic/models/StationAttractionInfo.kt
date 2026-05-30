package com.example.vkr.logic.models

data class StationAttractionInfo(
    var id: Long,
    var name: String,
    var price: Int,
    var urlRef: String,
    var distance: Int
) {}