package com.example.vkr.storage.repositories

import com.example.vkr.logic.dto.request.CellInfo

interface TelephoneRepository {
    fun getCurrentCellInfo(): CellInfo?
}