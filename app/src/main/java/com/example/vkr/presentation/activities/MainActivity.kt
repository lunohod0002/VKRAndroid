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

    @Inject
    lateinit var navigator: NavigatorImpl

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

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            val destId = destination.id
            val fromId = controller.previousBackStackEntry?.destination?.id

            var checkedItemId = when (destId) {
                R.id.screen_map,
                R.id.screen_station,
                R.id.screen_reference_info -> destId

                else -> R.id.menu_item_none
            }
            if (fromId == R.id.screen_map && destId == R.id.screen_station) {
                checkedItemId = R.id.menu_item_none
            }

            binding.bottomNavigationView.menu.findItem(checkedItemId)?.isChecked = true
        }
    }

    private fun handleNavigation(itemId: Int): Boolean {

        if (itemId == navController.currentDestination?.id) return true
        navController.navigate(itemId)
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
        navigator.unbind()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}