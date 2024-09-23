package com.grg.spotify.auth.networking

import com.grg.spotify.core.utils.Constants
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AccessTokenAuthenticator @Inject constructor(
    private val codeVerifierStore: com.grg.spotify.domain.networking.ICodeVerifierStore,
    private val spotifyAuthService: dagger.Lazy<SpotifyAuthService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshTokenResponse = runBlocking {
            spotifyAuthService.get().refreshAccessToken(
                refreshToken = codeVerifierStore.getRefreshToken().orEmpty(),
                clientId = Constants.CLIENT_ID
            )
        }
        return (refreshTokenResponse as? ApiResponse.Success)?.run {
            codeVerifierStore.saveAccessToken(accessToken = this.data.accessToken)
            codeVerifierStore.saveRefreshToken(refreshToken = this.data.refreshToken)
            response.request.newBuilder().header("Authorization", "Bearer ${data.accessToken}")
                .build()
        }
    }
}