package com.grg.spotify.presentation.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grg.core.presentation.ui.components.bottomnavbar.BottomNavBar
import com.grg.core.presentation.ui.components.bottomnavbar.BottomNavScreens

@Composable
fun HomeScreen(
    currentScreen: BottomNavScreens,
    onScreenSelected: (BottomNavScreens) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomNavBar(
        selectedScreen = currentScreen,
        onItemSelected = { onScreenSelected(it) }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 0xFF000000)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        currentScreen = BottomNavScreens.Home,
        onScreenSelected = {}
    )
}