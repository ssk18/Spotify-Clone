package com.grg.spotify.auth.repository

import android.net.Uri
import com.grg.core.utils.Constants
import com.grg.spotify.auth.model.toAccessTokenInfo
import com.grg.spotify.auth.networking.SpotifyAuthService
import com.grg.spotify.domain.model.AccessTokenInfo
import com.grg.spotify.domain.networking.ICodeChallengeProvider
import com.grg.spotify.domain.networking.ICodeVerifierStore
import com.grg.spotify.domain.repository.IAuthRepository
import java.net.URLEncoder
import java.util.UUID
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val codeChallengeProvider: ICodeChallengeProvider,
    private val codeStore: ICodeVerifierStore,
    private val spotifyAuthService: SpotifyAuthService
) : IAuthRepository {

    override fun requestAuthorization(
        clientId: String,
        redirectUri: String,
        scope: String,
        launchAuthScreen: (url: String) -> Unit
    ) {
        val codeChallenge = codeChallengeProvider.getCodeChallenge()
        val state = UUID.randomUUID().toString().also {
            codeStore.saveRequestState(it)
        }
        val authParams = mapOf(
            "client_id" to clientId,
            "response_type" to "code",
            "redirect_uri" to redirectUri,
            "scope" to scope,
            "code_challenge_method" to "S256",
            "state" to state,
            "code_challenge" to codeChallenge
        ).map {
            "${it.key}=${it.value}"
        }.joinToString(separator = "&")
        launchAuthScreen("${Constants.SPOTIFY_AUTH_URL}?$authParams")
    }

    override suspend fun postRequestAccess(
        authCode: String,
        redirectUri: String,
        clientId: String,
        codeVerifier: String
    ): Result<AccessTokenInfo> {
        return runCatching {
            spotifyAuthService.postRequestAccess(
                authCode = authCode,
                redirectUri = redirectUri,
                clientId = clientId,
                codeVerifier = codeVerifier
            ).toAccessTokenInfo()
        }
    }

    override suspend fun refreshToken(
        refreshToken: String,
        clientId: String
    ): Result<AccessTokenInfo> {
        return runCatching {
            spotifyAuthService.refreshAccessToken(
                refreshToken = refreshToken,
                clientId = clientId
            ).toAccessTokenInfo()
        }
    }
}
