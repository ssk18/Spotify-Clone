package com.grg.core.presentation.ui.components.bottomnavbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.grg.core.presentation.R

@Composable
fun BottomNavBar(
    onItemSelected: (BottomNavScreens) -> Unit,
    selectedScreen: BottomNavScreens
) {
    NavigationBar(
        containerColor = Color.Transparent
    ) {
        BottomNavScreens.entries.forEach { screen ->
            NavigationBarItem(
                selected = selectedScreen == screen,
                onClick = { onItemSelected(screen) },
                icon = {
                    Icon(
                        imageVector = if (selectedScreen == screen) {
                            screen.selectedIcon
                        } else {
                            screen.unselectedIcon
                        },
                        contentDescription = stringResource(R.string.bottom_navigation_icon),
                    )
                },
                label = {
                    Text(
                        text = screen.text
                    )
                }
            )
        }
    }
}

enum class BottomNavScreens(
    val text: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home(
        text = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    Search(
        text = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),
    Library(
        text = "Your Library",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    ),
    Create(
        text = "Create",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    ),

}