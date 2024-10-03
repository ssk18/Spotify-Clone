@file:OptIn(ExperimentalMaterial3Api::class)

package com.grg.spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.grg.core.presentation.ui.components.bottomnavbar.BottomNavScreens
import com.grg.core.presentation.ui.components.topappbar.TopAppBarScreens
import com.grg.core.presentation.ui.theme.MySpotifyTheme
import com.grg.spotify.presentation.ui.HomeScreen
import com.grg.spotify.ui.components.AppTopBar
import com.grg.spotify.ui.components.NavigationDrawerContent
import com.grg.spotify.ui.homescreen.HomeViewModel
import com.grg.spotify.ui.navigation.SpotifyNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MySpotifyTheme {
                MainScreen(navController = navController)
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var screenState by remember {
        mutableStateOf(TopAppBarScreens.All)
    }
    val coroutineScope = rememberCoroutineScope()
    val (currentScreen, setCurrentScreen) = rememberSaveable {
        mutableStateOf(BottomNavScreens.Home) // Default to the Home screen
    }
    val drawerWidth = 300.dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState,
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            ) {
                NavigationDrawerContent(
                    viewModel = viewModel
                )
            }
        },
        content = {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = drawerWidth.toPx() + drawerState.currentOffset
                    },
                topBar = {
                    AppTopBar(
                        viewModel = viewModel,
                        screenState = screenState,
                        onScreenStateChange = {
                            screenState = it
                        },
                        onProfileClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    )
                },
                bottomBar = {
                    HomeScreen(
                        currentScreen = currentScreen,
                        onScreenSelected = { setCurrentScreen(it) }
                    )
                }
            ) { padding ->
                SpotifyNavigation(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    )
}
