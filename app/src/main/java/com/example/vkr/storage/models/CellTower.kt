
package com.example.vkr.storage.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "cell",
        foreignKeys = [
                ForeignKey(
                        entity = StationEntity::class,
                        parentColumns = ["id"],
                        childColumns = ["stationId"],
                        onDelete = ForeignKey.CASCADE
                )
        ],
        indices = [
                Index(value = ["stationId"]),
                Index(value = ["cid", "lac", "mcc", "mnc", "radio"])
        ]
)
data class CellTower(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val stationId: Long,
        val lac: String?,
        val mcc: String?,
        val mnc: String?,
        val cid: String?,
        val radio: String?
)