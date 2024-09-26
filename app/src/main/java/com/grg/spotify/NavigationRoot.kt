package com.grg.spotify

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

//@Composable
//fun NavigationRoot(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//    val viewModel = viewModel<MainViewModel>()
//    val state = viewModel.state.collectAsStateWithLifecycle()
//    NavHost(navController = navController, startDestination = NavRoutes.AuthScreen) {
//        composable<NavRoutes.AuthScreen> {
//            Column {
//                Text(text = "it is working")
//            }
//        }
//    }
//}


//sealed interface NavRoutes {
//
//    @Serializable
//    data object AuthActivity: NavRoutes
//
//    @Serializable
//    data object
//
//
//}