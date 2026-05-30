package com.example.vkr.logic.repositories

import com.example.vkr.logic.dto.request.CellInfo

interface TelephoneRepository {
    fun getCurrentCellInfo(): CellInfo?
}