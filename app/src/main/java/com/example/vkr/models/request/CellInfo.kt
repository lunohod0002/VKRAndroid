package com.example.vkr.models.request

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

data class CellInfo(

    var lac: String? = null,
    var mcc: String? = null,
    var mnc: String? = null,
    var cid: String? = null,
    var radio: String? = null
)


@Entity(
    tableName = "cellInfo",
    foreignKeys = [
        ForeignKey(
            entity = StationEntity::class,
            parentColumns = ["id"],
            childColumns = ["stationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CellEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var lac: String? = null,
    var mcc: String? = null,
    var mnc: String? = null,
    var cid: String? = null,
    var radio: String? = null,
    var stationId: Long? = null // внешний ключ на StationEntity
)

@Entity(tableName = "station")
data class StationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String? = null
)


@Entity(tableName = "notFoundCell")
data class NotFoundCell(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var lac: String? = null,
    var mcc: String? = null,
    var mnc: String? = null,
    var cid: String? = null,
    var radio: String? = null,
    val status: String? = null,
    val stationId:Long? = null
)
object RadioType {
    const val GSM = "gsm"
    const val CDMA = "cdma"
    const val UMTS = "umts"
    const val LTE = "lte"
}

