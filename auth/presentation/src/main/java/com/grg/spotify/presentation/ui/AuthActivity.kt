package com.grg.spotify.presentation.ui

import AuthScreen
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.grg.core.presentation.ui.theme.MySpotifyTheme
import com.grg.spotify.auth.data.utils.Constants
import com.grg.spotify.auth.data.utils.Constants.AUTH_HOST
import com.grg.spotify.auth.data.utils.Constants.AUTH_SCHEME
import com.grg.spotify.domain.IAuthRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var authRepository: IAuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySpotifyTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        authRepository.requestAuthorization(
            clientId = Constants.client_id,
            scope = "user-read-private user-read-email",
            redirectUri = "$AUTH_SCHEME://$AUTH_HOST"
        ) {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            Log.d("AuthActivity123 in oncrete", "onCreate123 intent: ${intent?.data}")

            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)


            // Launch the custom tab with the authorization URL
            customTabsIntent.launchUrl(this, Uri.parse(it))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("AuthActivity123", "onCreate intent: ${intent?.data}")
        val uri = intent.data
        if (uri != null && uri.scheme == AUTH_SCHEME && uri.host == AUTH_HOST) {
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")

            if (code != null) {
                // Use the authorization code to request tokens
                Log.d("AuthActivity123", "Authorization Code: $code")
                // handleSpotifyAuthCode(code)
            } else if (error != null) {
                // Handle the error case
                Log.d("AuthActivity123", "Error: $error")
            }
        }
    }
}