package com.grg.spotify.presentation.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.grg.core.domain.DataState
import com.grg.core.presentation.ui.theme.MySpotifyTheme
import com.grg.core.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySpotifyTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthScreenRoot(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
        observeForToken()
    }

    private fun observeForToken() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.accessTokenFlow.filter { it is DataState.Success || it is DataState.Failure }
                    .collect { dataState ->
                        if (dataState is DataState.Success) {
                            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                intent.getParcelableExtra(
                                    KEY_DESTINATION_INTENT,
                                    ComponentName::class.java
                                )
                            } else {
                                @Suppress("DEPRECATION")
                                intent.getParcelableExtra(KEY_DESTINATION_INTENT)
                            }).let { component ->
                                Log.d("Access token for intent", "${dataState.result.accessToken}")
                                startActivity(Intent().apply {
                                    setComponent(component)
                                    putExtra(KEY_ACCESS_TOKEN, dataState.result.accessToken)
                                })
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Authentication error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("AuthActivity123", "onCreate intent: ${intent?.data}")
        authViewModel.handleAuthCallbackUri(intent.data, Constants.REDIRECT_URI)
    }

    companion object {
        private const val KEY_DESTINATION_INTENT = "destination_intent"
        private const val KEY_ACCESS_TOKEN = "access_token"

        fun createLaunchIntent(context: Context, destination: ComponentName): Intent =
            Intent(context, AuthActivity::class.java).apply {
                putExtra(KEY_DESTINATION_INTENT, destination)
            }
    }
}


