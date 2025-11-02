
package com.example.vkr.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import com.example.vkr.models.request.CellInfo
import com.example.vkr.models.request.RadioType


@SuppressLint("MissingPermission")
fun getCurrentCellInfo(context: Context): CellInfo? {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val allCellInfo= telephonyManager.allCellInfo
        .mapNotNull {
        when (it) {
            is CellInfoGsm -> getCellInfo(it)
            is CellInfoWcdma -> getCellInfo(it)
            is CellInfoLte -> getCellInfo(it)
            else -> null
        }
    }
    if (allCellInfo.isEmpty()){
        return null
    }
    println(allCellInfo.get(0))
    return allCellInfo.get(0)
    }

fun getCellInfo(info: CellInfoGsm): CellInfo {
    val cellInfo = CellInfo()
    cellInfo.radio = RadioType.GSM

    info.cellIdentity.let {
        val (mcc, mnc) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        } else {
            Pair(it.mcc, it.mnc)
        }
        cellInfo.mcc = mcc.toString()
        cellInfo.mnc = mnc.toString()
        cellInfo.lac= it.lac.toString()
        cellInfo.cid= it.cid.toString()

    }

    return cellInfo
}

fun getCellInfo(info: CellInfoWcdma): CellInfo {
    val cellInfo = CellInfo()

    cellInfo.radio = RadioType.CDMA

    info.cellIdentity.let {
        val (mcc, mnc) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        } else {
            Pair(it.mcc, it.mnc)
        }
        cellInfo.mcc = mcc.toString()
        cellInfo.mnc = mnc.toString()
        cellInfo.lac= it.lac.toString()
        cellInfo.cid= it.cid.toString()
    }

    return cellInfo
}

fun getCellInfo(info: CellInfoLte): CellInfo {
    val cellInfo = CellInfo()

    cellInfo.radio = RadioType.LTE

    info.cellIdentity.let {

        val (mcc, mnc) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        } else {
            Pair(it.mcc, it.mnc)
        }
        cellInfo.mcc = mcc.toString()
        cellInfo.mnc = mnc.toString()
        cellInfo.lac= it.tac.toString()
        cellInfo.cid= it.ci.toString()
    }

    return cellInfo
}
