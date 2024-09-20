package com.grg.spotify.auth.data.repository

import com.grg.spotify.auth.data.mappers.toDomain
import com.grg.spotify.auth.data.networking.SpotifyAuthService
import com.grg.spotify.auth.data.utils.Constants
import com.grg.spotify.domain.model.AccessTokenInfo
import com.grg.spotify.domain.repository.IAuthRepository
import com.grg.spotify.domain.networking.ICodeChallengeProvider
import com.grg.spotify.domain.networking.ICodeVerifierStore
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
        return spotifyAuthService.postRequestAccess(
            authCode = authCode,
            redirectUri = redirectUri,
            clientId = clientId,
            codeVerifier = codeVerifier
        ).toDomain()
    }

    override suspend fun refreshToken(
        grantType: String,
        refreshToken: String,
        clientId: String
    ): Result<AccessTokenInfo> {
        return spotifyAuthService.refreshAccessToken(
            grantType, refreshToken, clientId
        ).toDomain()
    }
}
