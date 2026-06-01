package com.example.vkr.storage.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import com.example.vkr.logic.dto.request.CellInfo
import com.example.vkr.logic.dto.request.RadioType
import com.example.vkr.logic.repositories.TelephoneRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class TelephoneRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context : Context) : TelephoneRepository {
    @SuppressLint("MissingPermission")
    override fun getCurrentCellInfo(): CellInfo? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val allCellInfo = telephonyManager.allCellInfo
            ?.mapNotNull {
                when (it) {
                    is CellInfoGsm -> getCellInfoGsm(it)
                    is CellInfoWcdma -> getCellInfoWcdma(it)
                    is CellInfoLte -> getCellInfoLte(it)
                    else -> null
                }
            } ?: emptyList()
//        if (allCellInfo.isEmpty()){
//            return null
//        }

        return CellInfo("5088","250","1","197125378","LTE")
    }
    private fun getCellInfoGsm(info: CellInfoGsm): CellInfo {
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

    private fun getCellInfoWcdma(info: CellInfoWcdma): CellInfo {
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

    private fun getCellInfoLte(info: CellInfoLte): CellInfo {
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

}