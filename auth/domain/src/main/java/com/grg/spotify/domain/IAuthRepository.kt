package com.grg.spotify.domain

interface IAuthRepository {
    fun requestAuthorization(
        clientId: String,
        redirectUri: String,
        scope: String,
        launchAuthScreen: (url: String) -> Unit
    )
}