package com.example.vkr.logic.navigation

import androidx.navigation.NavDirections

sealed class NavigationCommand {
    data class To(val directions: NavDirections) : NavigationCommand()
    data object Back : NavigationCommand()
}