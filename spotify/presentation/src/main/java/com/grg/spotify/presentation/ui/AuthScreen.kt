@file:OptIn(ExperimentalMaterial3Api::class)

package com.grg.spotify.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grg.core.presentation.ui.components.SpotifyActionButton
import com.grg.core.presentation.ui.theme.MySpotifyTheme
import com.grg.core.presentation.ui.theme.displayFontFamily
import com.grg.spotify.core.utils.Constants
import com.grg.spotify.presenation.R
import com.grg.spotify.presentation.ui.AuthViewModel

@Composable
fun AuthScreenRoot(
    modifier: Modifier = Modifier
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current
    AuthScreen(
        isLoading = false,
        onRequestAuthorization = {
            viewModel.requestAuthorization(
                redirectUri = Constants.REDIRECT_URI
            ) {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // Launch the custom tab with the authorization URL
                customTabsIntent.launchUrl(context, Uri.parse(it))
            }
        }
    )
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onRequestAuthorization: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.spotify),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = displayFontFamily
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.spotify_primary_logo),
                contentDescription = stringResource(R.string.spotify_logo),
                alignment = Alignment.Center,
                modifier = Modifier
                    .widthIn(150.dp)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            SpotifyActionButton(
                text = stringResource(R.string.authenticate),
                isLoading = isLoading,
                enabled = true,
                onClick = onRequestAuthorization,
                modifier = Modifier
                    .width(IntrinsicSize.Min)
            )
        }
    }

}

@Preview
@Composable
fun AuthScreenPreview() {
    MySpotifyTheme {
        AuthScreen(
            isLoading = false,
            onRequestAuthorization = {}
        )
    }
}
