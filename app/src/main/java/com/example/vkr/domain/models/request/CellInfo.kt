package com.example.vkr.domain.models.request

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




object RadioType {
    const val GSM = "GSM"
    const val CDMA = "CDMA"
    const val LTE = "LTE"
}

