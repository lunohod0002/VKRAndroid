package com.example.vkr
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.browse.MediaBrowser
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

import com.example.myapplication.R
import com.example.vkr.models.State

import com.example.vkr.models.request.CellInfo
import com.example.vkr.repositories.CellRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var button_find: Button
    private lateinit var playerView: PlayerView
    private lateinit var mapView: WebView

    private lateinit var progressBar: ProgressBar

    private lateinit var cellRepository: CellRepository
    private lateinit var text_address: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        button_find = findViewById(R.id.button_find)
        progressBar = findViewById(R.id.progressBar)
        text_address = findViewById(R.id.text_address)
        initView()
        requestPermission()
        val player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.player)
        playerView.player = player
        val mediaItem = MediaItem.fromUri("https://sanstv.ru/test/audio/test.mp3")
// Set the media item to be played.
        player.setMediaItem(mediaItem)
// Prepare the player.
// Prepare the player.
        player.prepare()
// Start the playback.
        player.play()
        //   initLocationLiveData()
        lifecycleScope.launch(Dispatchers.IO) {
            val db = (application as App).getDb()
            cellRepository = CellRepository(db.cellDao())

        }

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

//        val cellInfo = getCurrentCellInfo(this)
//          if (cellInfo != null) {
//            fetchLocation(cellInfo)
//        }
//        fetchLocation(CellInfo("2","@","#","#","#"))

    }


    private fun requestPermission() {
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

    @SuppressLint("SetTextI18n")
    private fun fetchLocation(cellInfo: CellInfo) {
        //TODO узнать, почему программма падает на return          return@launch



        lifecycleScope.launch(Dispatchers.IO) {

            val cell = cellRepository.getCellAllInfo(
                lac = cellInfo.lac!!,
                mcc = cellInfo.mcc!!,
                mnc = cellInfo.mnc!!,
                cid = cellInfo.cid!!,
                radio = cellInfo.radio!!
            )
            if (cell!=null) {
                text_address.text = cell.station
                //TODO узнать, почему программма падает на return          return@launch
            } else{
                runOnUiThread(object : Runnable {
                    override fun run() {
                        text_address.text="Не удалось определить станцию: $cellInfo"

                       }
                })



            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
