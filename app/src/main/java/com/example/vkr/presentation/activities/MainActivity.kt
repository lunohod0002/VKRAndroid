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
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.vkr.App
import com.example.vkr.network.dto.StationData
import com.example.vkr.presentation.fragments.StationFragmentArgs
import com.example.vkr.logic.viewmodels.MainActivityViewModel
import com.example.vkr.network.api.AuthEvents
import com.example.vkr.storage.TokenStorage
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var refTapCount = 0
    private var refFirstTapAt = 0L

    companion object {
        private const val SECRET_TAP_WINDOW_MS = 5_000L
        private const val SECRET_TAP_REQUIRED = 5
    }
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.Factory(this, (application as App).getDb().cellDao())
    }

    // Сработает в любом случае (разрешил или запретил)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        openMapScreen()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Прячем меню на время показа системного окна (чисто для эстетики)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            handleNavigation(item.itemId, navController)
        }
        binding.bottomNavigationView.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.screen_reference_info) {
                registerReferenceInfoTap(navController)
            }
        }
        if (savedInstanceState == null) {

            checkPermissionAndOpenMap()
        }
        lifecycleScope.launch {
            AuthEvents.logoutEvents.collect {
                findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)
            }
        }
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            val destId = destination.id
            val fromId = controller.previousBackStackEntry?.destination?.id

            // Управление видимостью
            binding.bottomNavigationView.visibility =
                if (destId == R.id.emptyFragment) View.GONE else View.VISIBLE

            when (destId) {

                else -> {
                    // Для всех остальных экранов, кроме screen_reference_info, снимаем выделение
                    if (destId != R.id.screen_reference_info && destId != R.id.screen_map && destId !=R.id.screen_station ) {
                        binding.bottomNavigationView.menu.findItem(R.id.menu_item_none)?.isChecked =  true
                    }
                }
            }
        }
    }
    private fun registerReferenceInfoTap(navController: NavController) {
        val now = System.currentTimeMillis()
        if (now - refFirstTapAt > SECRET_TAP_WINDOW_MS) {
            // Окно истекло — начинаем заново с этого тапа
            refTapCount = 1
            refFirstTapAt = now
        } else {
            refTapCount++
        }

        if (refTapCount >= SECRET_TAP_REQUIRED) {
            refTapCount = 0
            refFirstTapAt = 0L
            decideStartDestination(navController)
        }
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

        // ВЫПОЛНЯЕМ ПЕРЕХОД. Только в этот момент создастся MapFragment и вызовется его onViewCreated
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHost.navController.navigate(R.id.action_emptyFragment_to_screen_map)
    }
    private fun decideStartDestination(navController: NavController) {
        val tokenStorage = TokenStorage(applicationContext)
        lifecycleScope.launch {
            val isLoggedIn = tokenStorage.isRefreshTokenValid()
            if (isLoggedIn) {
                navController.navigate(R.id.addAttractionFragment)
            } else {
                // Refresh истёк или его нет — на логин
                navController.navigate(R.id.loginFragment)
            }
        }
    }
    private fun handleNavigation(itemId: Int, navController: NavController): Boolean {
        val previousItemId = binding.bottomNavigationView.selectedItemId

        when (itemId) {
            R.id.screen_station -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this@MainActivity,
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
             R.id.screen_reference_info -> {
                registerReferenceInfoTap(navController)
                 navController.navigate(itemId)
                 return true

             }
            else -> {
                navController.navigate(itemId)
                return true
            }
        }
    }
}