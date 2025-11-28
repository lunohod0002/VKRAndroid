package com.example.vkr.domain.repositories

import com.example.vkr.domain.models.request.CellInfo

interface TelephoneRepository {
    fun getCurrentCellInfo(): CellInfo?
}