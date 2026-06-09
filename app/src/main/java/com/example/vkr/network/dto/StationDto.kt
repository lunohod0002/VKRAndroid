package com.example.vkr.network.dto;

// network/dto/StationDto.kt

data class AllStationsResponse(
        val stations: List<StationDto>
)
// network/dto/StationResponseDto.kt

data class StationResponseDto(
        val stations: List<StationDto>
)

data class StationDto(
        val id: Long,
        val name: String,
        val branch: String,
        val latitude: Double,
        val longitude: Double,
        val cellTowers: List<CellTowerDto>
)

data class CellTowerDto(
        val cid: String,
        val lac: String,
        val mcc: String,
        val mnc: String,
        val radio: String
)