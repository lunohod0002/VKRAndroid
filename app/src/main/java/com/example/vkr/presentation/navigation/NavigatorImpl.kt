package com.example.vkr.presentation.navigation

import androidx.navigation.NavController
import com.example.vkr.logic.navigation.AppNavigator
import com.example.vkr.logic.navigation.NavigationCommand
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor() : AppNavigator {

    private var navController: NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        this.navController = null
    }

    override fun navigate(command: NavigationCommand) {
        val controller = navController ?: return


        when (command) {
            is NavigationCommand.To -> {
                val currentDestination = controller.currentDestination
                val action = command.directions.actionId
                if (currentDestination?.getAction(action) != null) {
                    controller.navigate(command.directions)
                }
            }
            is NavigationCommand.Back -> {
                controller.popBackStack()
            }
        }
    }
}