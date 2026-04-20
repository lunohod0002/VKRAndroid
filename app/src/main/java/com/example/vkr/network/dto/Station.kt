package com.example.vkr.network.dto

class Station(
    var name: String,
    var branch: String,
    var extraServices: List<String>,
    var address: String,
    var description: String,
    var imagesRef:List<String>,
    var videosRef:List<String>,
    var audiosRef:List<String>
) {}