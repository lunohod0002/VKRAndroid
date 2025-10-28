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




object RadioType {
    const val GSM = "gsm"
    const val CDMA = "cdma"
    const val UMTS = "umts"
    const val LTE = "lte"
}

