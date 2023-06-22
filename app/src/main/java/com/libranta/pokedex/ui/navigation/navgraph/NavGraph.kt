package com.libranta.pokedex.ui.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.libranta.pokedex.ui.navigation.screens.Screen
import com.libranta.pokedex.ui.navigation.screens.feed.FeedScreen
import com.libranta.pokedex.ui.navigation.screens.settings.SettingsScreen

@Composable
fun NavGraph (
    navController: NavHostController,
    startDestination : String
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Screen.Feed.route){
            //FeedScreen()
        }
        composable(Screen.Settings.route){
            //SettingsScreen()
        }
    }
}