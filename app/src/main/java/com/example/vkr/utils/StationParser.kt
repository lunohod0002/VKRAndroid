package com.example.vkr
import java.nio.file.*
import java.util.zip.GZIPInputStream
import android.content.Context
import android.os.Build
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.vkr.models.Station
import com.example.vkr.models.request.CellInfo
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object StationParser {

    fun parseStations(context: Context): List<Station> {
        val stations = mutableListOf<Station>()
        val jsonFile = context.resources.openRawResource(R.raw.stations)
        val jsonString = InputStreamReader(jsonFile).readText()

        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val latitude = obj.getDouble("latitude")
            val longitude = obj.getDouble("longitude")
            stations.add(Station(name, latitude, longitude))
        }

        return stations
    }
//    fun fetchLocation(context: Context): List<List<String>> {
//
//        val lines = readCsvFromGzFromAssets(context)
//        return lines.map { it.split(",") }
//    }
////    fun readCsvFromGzFromAssets(context: Context): List<String> {
////        return context.resources.openRawResource(R.raw.database).use { rawStream ->
////            GZIPInputStream(rawStream).bufferedReader().use { it.readLines() }
////        }
////    }


}