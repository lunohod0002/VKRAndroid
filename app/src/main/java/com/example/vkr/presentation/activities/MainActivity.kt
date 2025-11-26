package com.example.vkr.presentation.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication.R
import com.example.vkr.App
import com.example.vkr.data.repositories.CellRepositoryImpl
import com.example.vkr.domain.models.request.CellInfo
import com.example.vkr.domain.repositories.CellRepository
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//        MapKitFactory.initialize(this)
//        setContentView(R.layout.activity_main)
//        mapView = findViewById(R.id.mapView)
//        button_find = findViewById(R.id.button_find)
//        progressBar = findViewById(R.id.progressBar)
//
//        text_address = findViewById(R.id.text_address)
//        initView()
//        requestPermission()
//        val player = ExoPlayer.Builder(this).build()
//        playerView = findViewById(R.id.player)
//        playerView.player = player
//        val mediaItem = MediaItem.fromUri("http://192.168.104.93:8080/api/media/download/ya-tebya-lyublyu (1).mp3")
//// Set the media item to be played.
//        player.setMediaItem(mediaItem)
//// Prepare the player.
//// Prepare the player.
//        player.prepare()
//// Start the playback.
//        player.play()
//        //   initLocationLiveData()
//        lifecycleScope.launch(Dispatchers.IO) {
//            val db = (application as App).getDb()
//            cellRepository = CellRepositoryImpl(db.cellDao())
//
//        }






}