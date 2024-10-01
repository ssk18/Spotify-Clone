@file:OptIn(ExperimentalMaterial3Api::class)

package com.grg.spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grg.core.domain.DataState
import com.grg.core.domain.orElse
import com.grg.core.presentation.ui.components.bottomnavbar.BottomNavScreens
import com.grg.core.presentation.ui.components.navigationdrawer.NavigationDrawerScreens
import com.grg.core.presentation.ui.components.navigationdrawer.ProfileNameIcon
import com.grg.core.presentation.ui.components.topappbar.CustomTopAppBar
import com.grg.core.presentation.ui.components.topappbar.TopAppBarScreens
import com.grg.core.presentation.ui.theme.MySpotifyTheme
import com.grg.spotify.presentation.ui.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val accessToken = intent.getStringExtra(SplashActivity.KEY_ACCESS_TOKEN).orElse { "" }
        setContent {
            MySpotifyTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun NavigationDrawerContents(
    userName: String,
    userInitials: Char,
    selectedScreen: NavigationDrawerScreens? = null,
    onScreenSelected: (NavigationDrawerScreens) -> Unit,
    onHeaderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        NavigationHeader(userInitials = userInitials, userName = userName, onHeaderClick)
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        NavigationDrawerScreens.entries.forEach { screen ->
            NavigationDrawerItem(
                label = {
                    Text(text = screen.text)
                },
                selected = selectedScreen == screen,
                onClick = {
                    onScreenSelected(screen)
                },
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = screen.text)
                }
            )
        }
    }
}

@Composable
fun NavigationHeader(
    userInitials: Char,
    userName: String,
    onIconClick: () -> Unit
) {
    ConstraintLayout(
        createHeaderConstraintSet(),
        modifier = Modifier.padding(start = 10.dp, top = 10.dp)
    ) {
        ProfileNameIcon(userInitials, onIconClick)
        Text(text = userName, modifier = Modifier.layoutId("name"), color = Color.White)
        Text(
            text = stringResource(R.string.view_profile),
            modifier = Modifier.run { layoutId(stringResource(R.string.viewprofile)) },
            color = Color.White
        )
    }
}

private fun createHeaderConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val profileIcon = createRefFor("profileIcon")
        val name = createRefFor("name")
        val viewProfile = createRefFor("viewProfile")
        constrain(profileIcon) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
        }
        constrain(name) {
            start.linkTo(profileIcon.end, 8.dp)
            top.linkTo(parent.top)
        }
        constrain(viewProfile) {
            top.linkTo(name.bottom, 2.dp)
            start.linkTo(profileIcon.end, 8.dp)
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var screenState by remember {
        mutableStateOf(TopAppBarScreens.All)
    }
    val coroutineScope = rememberCoroutineScope()
    var selectedFilter by remember { mutableStateOf("All") }
    val (currentScreen, setCurrentScreen) = rememberSaveable {
        mutableStateOf(BottomNavScreens.Home) // Default to the Home screen
    }
    val drawerWidth = 300.dp
    val userProfileState by viewModel.userProfileDetailsFlow.collectAsStateWithLifecycle()
    val state = viewModel.state

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState,
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            ) {
                when(userProfileState) {
                    is DataState.Success -> {
                        NavigationDrawerContents(
                            userName = state.userName,
                            userInitials = state.userName[0],
                            onScreenSelected = {},
                            onHeaderClick = {}
                        )
                    }

                    is DataState.Failure -> {
                        Text(text = "Error loading profile: ${(userProfileState as DataState.Failure).throwable.message}")
                    }
                    DataState.Loading -> {
                        CircularProgressIndicator()
                    }
                    DataState.Uninitialized -> {
                        CircularProgressIndicator()
                    }
                }
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
                    when(userProfileState) {
                        is DataState.Success -> {
                            CustomTopAppBar(
                                userInitials = state.userName[0],
                                selectedText = screenState.text,
                                onTextSelected = {
                                    selectedFilter = it
                                },
                                onProfileClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        }

                        is DataState.Failure -> {
                            Text(text = "Error loading profile: ${(userProfileState as DataState.Failure).throwable.message}")
                        }
                        DataState.Loading -> {
                            CircularProgressIndicator()
                        }
                        DataState.Uninitialized -> {
                            CircularProgressIndicator()
                        }
                    }
                },
                bottomBar = {
                    HomeScreen(
                        currentScreen = currentScreen,
                        onScreenSelected = { setCurrentScreen(it) }
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Text(
                        text = ""
                    )
                }
            }
        }
    )
}


@Preview
@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview() {
    MySpotifyTheme {
        MainScreen()
    }
}


fun main() {
    val a = mutableListOf(1, 2)
    a.add(3)
    println("${System.identityHashCode(a)}")
}
