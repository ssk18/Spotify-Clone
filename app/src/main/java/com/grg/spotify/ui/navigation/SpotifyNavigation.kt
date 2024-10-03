package com.grg.spotify.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grg.spotify.ui.homescreen.HomeScreen
import com.grg.spotify.ui.homescreen.HomeViewModel
import kotlinx.serialization.Serializable

@Composable
fun SpotifyNavigation(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Home
    ) {
        composable<Route.Home> {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
            )
        }
    }
}

sealed interface Route {
    @Serializable
    data object Home: Route

    @Serializable
    data object Search: Route

    @Serializable
    data object Library: Route
}