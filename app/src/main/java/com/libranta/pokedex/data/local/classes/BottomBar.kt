package com.libranta.pokedex.data.local.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.libranta.pokedex.R
import com.libranta.pokedex.ui.navigation.screens.Screen

sealed class BottomBar(
    val route: String,
    val title: Int,
    val icon: ImageVector
) {
    object Feed : BottomBar(
        route = Screen.Feed.route,
        title = R.string.tab_feed,
        icon = Icons.Filled.BrowseGallery
    )

    object Settings : BottomBar(
        route = Screen.Settings.route,
        title = R.string.tab_settings,
        icon = Icons.Filled.Settings
    )

}