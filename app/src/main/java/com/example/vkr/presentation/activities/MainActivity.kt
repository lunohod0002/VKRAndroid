package com.example.vkr.presentation.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.vkr.App
import com.example.vkr.network.dto.StationData
import com.example.vkr.presentation.fragments.StationFragmentArgs
import com.example.vkr.logic.viewmodels.MainActivityViewModel
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
                return true
            }
            else -> {
                navController.navigate(itemId)
                return true
            }
        }
    }







}