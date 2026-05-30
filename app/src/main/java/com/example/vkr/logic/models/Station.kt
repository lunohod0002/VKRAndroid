package com.example.vkr.logic.models

data class Station(
    var id : Long,
    var name: String,
    var branch: String,
    var extraServices: List<String>,
    var description: String,
    var imagesRef:List<String>,
    var videosRef:List<String>,
    var audiosRef:List<String>,
    var attractionResponseList:List<StationAttractionInfo>
) {}