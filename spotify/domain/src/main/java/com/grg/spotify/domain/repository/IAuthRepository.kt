package com.grg.spotify.domain.repository

import com.grg.spotify.domain.model.AccessTokenInfo

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

    suspend fun refreshToken(
       refreshToken: String,
       clientId: String
    ): Result<AccessTokenInfo>

}