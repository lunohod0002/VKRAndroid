package com.example.vkr
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.vkr.models.CellEntityUpdate
import com.example.vkr.models.State
import com.example.vkr.models.Station
import com.example.vkr.models.request.CellInfo
import com.example.vkr.models.response.CellLocation
import com.example.vkr.repositories.CellRepository
import com.example.vkr.repositories.CellStationRepository
import com.example.vkr.utils.getCurrentCellInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
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
    private var count: Int = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var text_location: TextView
    private lateinit var nearby_stations: TextView
    private lateinit var notFoundCellRepository: CellStationRepository

    private lateinit var cellRepository: CellRepository
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
        lifecycleScope.launch(Dispatchers.IO) {
            val db = (application as App).getDb()
            cellRepository = CellRepository(db.cellDao())
            notFoundCellRepository = CellStationRepository(db.notFoundCellDao())

        }


//        lifecycleScope.launch {
//            notFoundCellRepository.allLogs.collect { logs ->
//            }
//        }

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
                //   showLocationInfo(state.response)
            }
        }
    }

//    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val latDistance = Math.toRadians(lat2 - lat1)
//        val lonDistance = Math.toRadians(lon2 - lon1)
//
//        val a = sin(latDistance / 2).pow(2) +
//                cos(Math.toRadians(lat1)) *
//                cos(Math.toRadians(lat2)) *
//                sin(lonDistance / 2).pow(2)
//
//        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//        return EARTH_RADIUS * c
//    }

//    fun getNearbyStationNames(
//        stations: List<Station>,
//        targetLat: Double?,
//        targetLon: Double?,
//        radiusMeters: Double = 1500.0
//    ): List<String>? {
//        if (targetLon != null && targetLat != null) {
//
//            return stations
//                .filter { station ->
//                    haversineDistance(
//                        targetLat,
//                        targetLon,
//                        station.latitude,
//                        station.longitude
//                    ) <= radiusMeters
//                }
//                .map { it.name }
//        } else {
//            println("ELSE")
//            return null
//        }
//    }

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
        //  if (cellInfo != null) {
        fetchLocation(CellInfo("1", "1", "1", "1", "!"))
        //}
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

        var stations: List<CellEntityUpdate>

//        lifecycleScope.launch {
//            val id = notFoundCellRepository.insert("2","250","01","!","!","founded")
//            Log.d("DB", "Inserted row ID: ${id}")
//        }
        lifecycleScope.launch(Dispatchers.IO) {

            val id = cellRepository.insert("1", "250", "01", "17754", "LTE")

            Log.d("DB", "Inserted row ID: ${id}")
        }
        lifecycleScope.launch(Dispatchers.IO) {

            val cell = cellRepository.allCells
            println(cell)
        }


//
//            val allStations=notFoundCellRepository.allStations
//            if (allStations.count() == 0) {
//                lifecycleScope.launch {
//                    val id = notFoundCellRepository.insert(
//                        lac = cellInfo.lac!!,
//                        mcc = cellInfo.mcc!!,
//                        mnc = cellInfo.mnc!!,
//                        cellInfo.cid!!,
//                        radio = cellInfo.radio!!,
//                    )
//                    Log.d("DB", "Inserted row ID: ${id}")
//                }
//                return
//            }
//            else if (allStations.get(-1).station==null ){
//                lifecycleScope.launch {
//                    val id = notFoundCellRepository.insert(
//                        lac = cellInfo.lac!!,
//                        mcc = cellInfo.mcc!!,
//                        mnc = cellInfo.mnc!!,
//                        cellInfo.cid!!,
//                        radio = cellInfo.radio!!,
//                    )
//                    Log.d("DB", "Inserted row ID: ${id}")
//                }
//                return


//                  //TODO : отправлять файл с ненайденными станциями и найденной на сервер и обновлять базу
//
//                val cellLocation = CellLocation(lat = station.get(7).lowercase().toDouble(), lon = station.get(6).lowercase().toDouble() )
//                _locationLiveData.value = State.Success(cellLocation)
//            }}
//        if (!found){
//            val sb = StringBuilder()
//            sb.append(cellInfo.toString())
//            sb.append("not-found")
//            writeFileOnInternalStorage(this,"no-stations.txt",cellInfo.toString())
//
//            count++
//        }
//        val content = readFileFromInternalStorage(this, "no-stations.txt")
//        println("Содержимое файла:\n$content")
//        if (count==3){
//            val dir = File(this.filesDir, "mydir")
//            val file = File(dir, "no-stations.txt")
//            val deleted = file.delete()
//
//        }
    }


    }

//    fun updateCellInfoOnServer(file: File){
//        val requestFile: RequestBody = RequestBody.create(file, MediaType.parse("text/plain"))
//
//
//// Создаём MultipartBody.Part
//        val body = MultipartBody.Part.createFormData("file", file.getName(), requestFile)
//
//
//// Вызываем API
//        val apiService: UpdateCellInfoService = create(UpdateCellInfoService::class.java)
//        val call: Call<ResponseBody?> = apiService.uploadFile(body)
//
//        call.enqueue(object : Callback<ResponseBody?>() {
//            public override fun onResponse(
//                call: Call<ResponseBody?>?,
//                response: Response<ResponseBody?>
//            ) {
//                if (response.isSuccessful()) {
//                    Log.d("Upload", "Файл успешно загружен")
//                } else {
//                    Log.e("Upload", "Ошибка: " + response.code())
//                }
//            }
//
//            public override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
//                Log.e("Upload", "Сбой сети: " + t.message)
//            }
//        })
//    }

//        @SuppressLint("SetTextI18n")
//    private fun showLocationInfo(cellLocation: CellLocation) {
//        text_location.text = getString(
//            R.string.text_location_format,
//            cellLocation.lat,
//            cellLocation.lon
//        )
//        val nearbyNames = getNearbyStationNames(
//            stations,
//            targetLat = cellLocation.lat,
//            targetLon = cellLocation.lon
//        )
//        nearby_stations.text = nearbyNames.toString()
//        text_address.text = "address"
//        mapView.loadUrl(
//            "https://www.google.com/maps/place/${cellLocation.lat},${cellLocation.lon}"
//        )
//    }

