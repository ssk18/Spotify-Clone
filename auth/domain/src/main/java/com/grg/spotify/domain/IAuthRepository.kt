package com.grg.spotify.domain

interface IAuthRepository {
    fun requestAuthorization(
        clientId: String,
        redirectUri: String,
        scope: String,
        launchAuthScreen: (url: String) -> Unit
    )

    suspend fun postRequestAccess(
        authCode: String,
        redirectUri: String,
        clientId: String,
        codeVerifier: String
    ): Result<AccessTokenInfo>


}