package com.example.vkr.models;

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cell")
data class CellEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        var lac: String? = null,
        var mcc: String? = null,
        var mnc: String? = null,
        var cid: String? = null,
        var radio: String? = null,
        var station: String? = null,
        var branch: Int? = null

)

@Entity(tableName = "cellUpdate")
data class CellEntityUpdate(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        var lac: String? = null,
        var mcc: String? = null,
        var status : String? = null,
        var mnc: String? = null,
        var cid: String? = null,
        var radio: String? = null,
        var station: String? = null,
        var branch: Int? = null,
        )