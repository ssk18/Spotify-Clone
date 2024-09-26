package com.grg.spotify

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grg.spotify.SplashActivity.Companion.KEY_ACCESS_TOKEN
import com.grg.spotify.presentation.ui.AuthActivity

@Composable
fun SplashScreenContent(
    viewModel: SplashViewModel,
) {
    val accessTokenState by viewModel.accessTokenFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    when (accessTokenState) {
        is AccessTokenState.Loading -> {
            Text("Loading...")
        }

        is AccessTokenState.Empty -> {
            startAuthActivity(context as Activity)
        }

        is AccessTokenState.Valid -> {
            startMainActivity(
                context as Activity,
                (accessTokenState as AccessTokenState.Valid).token
            )
        }
    }
}

private fun startMainActivity(activity: Activity, accessToken: String) {
    val intent = Intent(activity, MainActivity::class.java).apply {
        putExtra(KEY_ACCESS_TOKEN, accessToken)
    }
    activity.startActivity(intent)
    activity.finish()
}

private fun startAuthActivity(activity: Activity) {
    val authIntent =
        AuthActivity.createLaunchIntent(activity, ComponentName(activity, MainActivity::class.java))
    activity.startActivity(authIntent)
    activity.finish()
}