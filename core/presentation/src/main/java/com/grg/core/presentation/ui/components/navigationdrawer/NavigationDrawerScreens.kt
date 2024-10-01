package com.grg.core.presentation.ui.components.navigationdrawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationDrawerScreens(
    val text: String,
    val icon: ImageVector
) {
    Add("Add account", Icons.Default.Add),
    New("What's new", Icons.Filled.Info),
    History("Listening history", Icons.Filled.Favorite),
    Settings("Settings and Privacy", Icons.Filled.Settings),
}