@file:OptIn(ExperimentalMaterial3Api::class)

package com.grg.spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grg.core.domain.orElse
import com.grg.core.presentation.ui.theme.MySpotifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val accessToken = intent.getStringExtra(SplashActivity.KEY_ACCESS_TOKEN).orElse { "" }
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

@Composable
fun MainScreen(
    accessToken: String,
    modifier: Modifier = Modifier
) {
    Column() {
        Text(
            text = "Hey $accessToken",
        )
    }
}
