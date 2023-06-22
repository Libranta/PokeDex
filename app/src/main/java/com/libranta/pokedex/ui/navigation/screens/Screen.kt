package com.libranta.pokedex.ui.navigation.screens

// Small directory of Screens for Navigation
sealed class Screen (val route : String){
    object Feed : Screen("feed_screen")
    object Settings : Screen("settings_screen")
}