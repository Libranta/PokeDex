package com.libranta.pokedex.data.local.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.libranta.pokedex.ui.navigation.screens.Screen

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Feed : BottomBar(
        route = Screen.Feed.route,
        title = "Feed",
        icon = Icons.Filled.BrowseGallery
    )

    object Settings : BottomBar(
        route = Screen.Settings.route,
        title = "Settings",
        icon = Icons.Filled.Settings
    )

}