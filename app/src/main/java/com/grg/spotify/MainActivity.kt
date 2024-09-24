@file:OptIn(ExperimentalMaterial3Api::class)

package com.grg.spotify

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.grg.core.presentation.ui.theme.MySpotifyTheme
import com.grg.spotify.domain.networking.ICodeVerifierStore
import com.grg.spotify.presenation.R
import com.grg.spotify.presentation.ui.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var codeVerifierStore: ICodeVerifierStore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val accessToken =
            intent.getStringExtra(KEY_ACCESS_TOKEN) ?: codeVerifierStore.getAccessToken()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                accessToken.isNullOrEmpty()
            }
        }
        if (accessToken.isNullOrEmpty()) {
            startMainActivity()
        } else {
            setContent {
                MySpotifyTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainScreen(
                            accessToken = accessToken,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun startMainActivity() {
        val authIntent =
            AuthActivity.createLaunchIntent(this, ComponentName(this, MainActivity::class.java))
        startActivity(authIntent)
        finish()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }
}

@Composable
fun MainScreen(
    accessToken: String,
    modifier: Modifier = Modifier
) {
    Column() {
        Text(
            text = accessToken,
        )
    }
}
