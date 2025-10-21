package com.example.vkr

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.vkr.OpenCellRepository
import com.example.vkr.models.Station
import com.example.vkr.models.request.CellInfo
import com.example.vkr.models.response.CellLocation
import com.example.vkr.CellLocationService
import com.example.vkr.models.State
import com.example.vkr.StationParser
import com.example.vkr.utils.getCurrentCellInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    private val _locationLiveData = MutableLiveData<State>()

    val locationLiveData: LiveData<State> = _locationLiveData
    private lateinit var stations: List<Station>
    private lateinit var button_find: Button
    private lateinit var mapView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var text_location: TextView
    private lateinit var nearby_stations: TextView

    private lateinit var text_address: TextView

    private val EARTH_RADIUS = 6371000.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        button_find = findViewById(R.id.button_find)
        progressBar = findViewById(R.id.progressBar)
        text_location = findViewById(R.id.text_location)
        text_address = findViewById(R.id.text_address)
        nearby_stations = findViewById(R.id.nearby_stations)
        stations = StationParser.parseStations(this)
        initView()

        requestPermission()
        initLocationLiveData()


    }

    private fun onStateChanged(state: State) {
        when (state) {
            is State.Loading -> {
                //
            }
            is State.Failed -> {
//
            }
            is State.Success -> {
                showLocationInfo(state.response)
            }
        }
    }
    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = sin(latDistance / 2).pow(2) +
                cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS * c
    }

    fun getNearbyStationNames(
        stations: List<Station>,
        targetLat: Double?,
        targetLon: Double?,
        radiusMeters: Double = 1500.0
    ): List<String>? {
        if (targetLon != null && targetLat != null) {

            return stations
                .filter { station ->
                    haversineDistance(
                        targetLat,
                        targetLon,
                        station.latitude,
                        station.longitude
                    ) <= radiusMeters
                }
                .map { it.name }
        } else {
            println("ELSE")
            return null
        }
    }
    private fun initLocationLiveData() {
        locationLiveData.observe(
            this,
            Observer(::onStateChanged)
        )
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        val mapView: WebView = findViewById(R.id.mapView)

        button_find.setOnClickListener {
            onClickFindLocation()
        }
        mapView.settings.javaScriptEnabled = true
    }


    private fun onClickFindLocation() {

        val cellInfo = getCurrentCellInfo(this)
        if (cellInfo != null) {
            fetchLocation(cellInfo)
        }
    }


    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET),
                1999
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2000
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                2001
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    2002


                )
            }
        }
    }

    private fun fetchLocation(cellInfo: CellInfo) {


        val repository = OpenCellRepository(
            service = CellLocationService()
        )
        fetchLocation(cellInfo)
        }
    

    @SuppressLint("SetTextI18n")
    private fun showLocationInfo(cellLocation: CellLocation) {
        text_location.text = getString(
            R.string.text_location_format,
            cellLocation.lat,
            cellLocation.lon
        )
        val nearbyNames = getNearbyStationNames(
            stations,
            targetLat = cellLocation.lat,
            targetLon = cellLocation.lon
        )
        nearby_stations.text = nearbyNames.toString()
        text_address.text = "address"
        mapView.loadUrl(
            "https://www.google.com/maps/place/${cellLocation.lat},${cellLocation.lon}"
        )
    }

}