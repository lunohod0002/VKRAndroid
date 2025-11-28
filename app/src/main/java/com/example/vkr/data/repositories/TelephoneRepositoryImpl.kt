package com.example.vkr.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import com.example.vkr.domain.models.request.CellInfo
import com.example.vkr.domain.models.request.RadioType
import com.example.vkr.domain.repositories.TelephoneRepository

class TelephoneRepositoryImpl(private val context : Context) : TelephoneRepository {
    @SuppressLint("MissingPermission")
    override fun getCurrentCellInfo(): CellInfo? {
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
        //return CellInfo(lac="660", mcc = "250",mnc="1",cid="7437",radio="GSM")

        if (allCellInfo.isEmpty()){
            return null
        }

        return allCellInfo.get(0)
    }

    private fun getCellInfo(info: CellInfoGsm): CellInfo {
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

    private fun getCellInfo(info: CellInfoWcdma): CellInfo {
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

    private fun getCellInfo(info: CellInfoLte): CellInfo {
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