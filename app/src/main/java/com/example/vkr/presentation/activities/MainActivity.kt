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
import androidx.navigation.navOptions
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.vkr.logic.navigation.AppNavigator
import com.example.vkr.presentation.navigation.NavigatorImpl


import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject  lateinit var navigator: NavigatorImpl
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* isGranted */

        openMapScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController
        navigator.bind(navController)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            handleNavigation(item.itemId)
        }

        if (savedInstanceState == null) {
            checkPermissionAndOpenMap()
        }

        navController.addOnDestinationChangedListener { controller  , destination, _ ->
            val destId = destination.id
            val fromId = controller.previousBackStackEntry?.destination?.id

            binding.bottomNavigationView.visibility =
                if (destId == R.id.emptyFragment) View.GONE else View.VISIBLE

            var checkedItemId = when (destId) {
                R.id.screen_map,
                R.id.screen_add_attraction,
                R.id.screen_station,
                R.id.screen_reference_info -> destId
                else -> R.id.menu_item_none
            }
            if (fromId == R.id.screen_map && destId ==R.id.screen_station){
                checkedItemId =R.id.menu_item_none
            }

            binding.bottomNavigationView.menu.findItem(checkedItemId)?.isChecked = true
        }
    }

    private fun checkPermissionAndOpenMap() {
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            openMapScreen()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun openMapScreen() {

        if (navController.currentDestination?.id == R.id.emptyFragment) {
            navController.navigate(R.id.action_emptyFragment_to_screen_map)
        }
    }

    private fun handleNavigation(itemId: Int): Boolean {

        if (itemId == navController.currentDestination?.id) return true

        if (itemId == R.id.screen_station){
            if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this, "Выдайте разрешение на определение местоположения",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        }
        navController.navigate(itemId)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



}