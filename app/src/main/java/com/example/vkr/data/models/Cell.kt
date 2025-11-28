package com.example.vkr.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cell")
data class CellEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        var lac: String? = null,
        var mcc: String? = null,
        var mnc: String? = null,
        var cid: String? = null,
        var radio: String? = null,
        var station: String? = null,
        var branch: Int? = null

)