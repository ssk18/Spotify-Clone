package com.grg.spotify.data.networking

import com.grg.core.utils.Constants
import com.grg.spotify.domain.networking.ICodeVerifierStore
import com.grg.spotify.domain.repository.IAuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AccessTokenAuthenticator @Inject constructor(
    private val codeVerifierStore: ICodeVerifierStore,
    private val spotifyAuthRepo: IAuthRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            spotifyAuthRepo.refreshToken(
                refreshToken = codeVerifierStore.getRefreshToken().orEmpty(),
                clientId = Constants.CLIENT_ID
            )
        }.getOrNull()?.let { accessTokenInfo ->
            codeVerifierStore.saveAccessToken(accessTokenInfo.accessToken)
            codeVerifierStore.saveRefreshToken(accessTokenInfo.refreshToken)
            response.request.newBuilder()
                .header("Authorization", "Bearer ${accessTokenInfo.accessToken}")
                .build()
        }
    }
}