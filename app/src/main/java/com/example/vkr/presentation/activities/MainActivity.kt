package com.example.vkr.presentation.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.Factory(this, (application as App).getDb().cellDao())
    }

    // Сработает в любом случае (разрешил или запретил)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this@MainActivity, "Не удалось определить текущую станцию.\n" +
                    "Выдайте разрешение на определение местоположения", Toast.LENGTH_SHORT).show()

            // Диалог закрыт -> открываем карту (MapFragment создастся только тут)
        }
        openMapScreen()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Прячем меню на время показа системного окна (чисто для эстетики)
        binding.bottomNavigationView.visibility = View.GONE

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            handleNavigation(item.itemId, navController)
        }

        // Проверяем права и запрашиваем
        checkPermissionAndOpenMap()
    }

    private fun checkPermissionAndOpenMap() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Права уже есть -> сразу открываем карту
            openMapScreen()
        } else {
            // Прав нет -> показываем системное окно.
            // Пользователь видит пустой экран + системный диалог.
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun openMapScreen() {
        // Показываем меню обратно
        binding.bottomNavigationView.visibility = View.VISIBLE

        // ВЫПОЛНЯЕМ ПЕРЕХОД. Только в этот момент создастся MapFragment и вызовется его onViewCreated
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHost.navController.navigate(R.id.action_emptyFragment_to_screen_map)
    }

    private fun handleNavigation(itemId: Int, navController: NavController): Boolean {
        when (itemId) {
            R.id.screen_station -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this@MainActivity, "Не удалось определить текущую станцию.\n" +
                            "Выдайте разрешение на определение местоположения", Toast.LENGTH_SHORT).show()
                    return true


                }
                else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val cell = viewModel.fetchCurrentLocation()
                        if (cell != null) {
                            val args = StationFragmentArgs(
                                STATION = StationData(
                                    cell.station!!,
                                    cell.branch!!
                                )
                            )
                            launch(Dispatchers.Main) {
                                navController.navigate(R.id.screen_station, args.toBundle())
                            }
                        } else {
                            launch(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Не удалось определить текущую станцию",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    return true
                }
            }
            else -> {
                navController.navigate(itemId)
                return true
            }
        }
    }
}