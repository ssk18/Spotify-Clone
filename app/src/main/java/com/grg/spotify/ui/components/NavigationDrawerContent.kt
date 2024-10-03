package com.grg.spotify.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grg.core.domain.DataState
import com.grg.core.presentation.ui.components.ErrorWithTryAgain
import com.grg.core.presentation.ui.components.LoadingDataIndicator
import com.grg.core.presentation.ui.components.navigationdrawer.NavigationDrawerScreens
import com.grg.core.presentation.ui.components.navigationdrawer.ProfileNameIcon
import com.grg.spotify.R
import com.grg.spotify.ui.homescreen.HomeViewModel

@Composable
fun NavigationDrawerContent(
    viewModel: HomeViewModel
) {
    val dataState by viewModel.userProfileFlow.collectAsStateWithLifecycle()
    when (val stateValue = dataState) {
        is DataState.Success -> {
            NavigationDrawerContents(
                userName = stateValue.result.userName,
                userInitials = stateValue.result.userName[0],
                onScreenSelected = {},
                onHeaderClick = {}
            )
        }

        is DataState.Failure -> {
            ErrorWithTryAgain(errorMessage = stateValue.throwable.message.orEmpty(), onTryAgain = viewModel.userProfileFlow::restart)
        }

        DataState.Loading -> {
            LoadingDataIndicator("Loading User Profile details...")
        }

        DataState.Uninitialized -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun NavigationDrawerContents(
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
private fun NavigationHeader(
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
