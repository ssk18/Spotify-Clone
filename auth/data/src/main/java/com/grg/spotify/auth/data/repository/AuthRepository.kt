package com.grg.spotify.auth.data.repository

import com.grg.spotify.auth.data.utils.Constants
import com.grg.spotify.auth.data.utils.Constants.AUTH_HOST
import com.grg.spotify.auth.data.utils.Constants.AUTH_SCHEME
import com.grg.spotify.domain.IAuthRepository
import com.grg.spotify.domain.ICodeChallengeProvider
import com.grg.spotify.domain.ICodeVerifierStore
import java.util.UUID
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val codeChallengeProvider: ICodeChallengeProvider,
    private val codeStore: ICodeVerifierStore
) :
    IAuthRepository {

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
}