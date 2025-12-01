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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.vkr.App
import com.example.vkr.data.repositories.CellRepositoryImpl
import com.example.vkr.data.repositories.TelephoneRepositoryImpl
import com.example.vkr.domain.models.StationData
import com.example.vkr.domain.models.request.CellInfo
import com.example.vkr.domain.repositories.CellRepository
import com.example.vkr.domain.repositories.TelephoneRepository
import com.example.vkr.presentation.fragments.StationFragmentArgs
import com.example.vkr.presentation.viewmodels.MainActivityViewModel
import com.example.vkr.presentation.viewmodels.MapViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private  val viewModel: MainActivityViewModel by viewModels { MainActivityViewModel.Factory(this,(application as App).getDb().cellDao() )}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            handleNavigation(item.itemId, navController)
        }
    }
    private fun handleNavigation(itemId: Int, navController: NavController): Boolean {
        when (itemId) {
            R.id.screen_station -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    val cell =  viewModel.fetchCurrentLocation()
                    if (cell!=null) {

                        val args= StationFragmentArgs(STATION= StationData(cell.station!!, cell.branch!!))
                        launch(Dispatchers.Main) {
                            navController.navigate(R.id.screen_station, args.toBundle())
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "Не удалось определить текущую станцию", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return false
            }
            else -> {
                navController.navigate(itemId)
                return true
            }
        }
    }







}