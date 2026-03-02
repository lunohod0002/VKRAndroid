package com.example.vkr.domain.services

import com.example.vkr.isolation.StationDetails

class TelephoneService {
    fun getCurrentStationNameAndBranch(): StationDetails? {
        return StationDetails("1","1")
    }
}