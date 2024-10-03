package com.grg.spotify.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grg.core.domain.DataState
import com.grg.core.presentation.ui.components.ErrorWithTryAgain
import com.grg.core.presentation.ui.components.LoadingDataIndicator
import com.grg.core.presentation.ui.components.topappbar.CustomTopAppBar
import com.grg.core.presentation.ui.components.topappbar.TopAppBarScreens
import com.grg.spotify.ui.homescreen.HomeViewModel

@Composable
fun AppTopBar(
    viewModel: HomeViewModel,
    screenState: TopAppBarScreens,
    onScreenStateChange: (TopAppBarScreens) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dataState by viewModel.userProfileFlow.collectAsStateWithLifecycle()
    AnimatedContent(
        targetState = dataState,
        label = "",
        modifier = modifier
    ) { targetState ->
        when (targetState) {
            is DataState.Success -> {
                CustomTopAppBar(
                    userInitials = targetState.result.userName[0],
                    selectedText = screenState.text,
                    onTextSelected = {
                        onScreenStateChange(it as TopAppBarScreens)
                    },
                    onProfileClick = {
                        onProfileClick()
                    },
                )
            }

            is DataState.Failure -> {
                ErrorWithTryAgain(errorMessage = targetState.throwable.message.orEmpty()) {
                    viewModel.userProfileFlow::restart
                }
            }

            is DataState.Loading, is DataState.Uninitialized -> {
                LoadingDataIndicator("Loading User Profile details...")
            }
        }
    }
}