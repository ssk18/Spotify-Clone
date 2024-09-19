package com.grg.spotify.auth.data.networking

import com.grg.spotify.domain.ICodeVerifierStore
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AccessTokenAuthenticator @Inject constructor(
    private val codeVerifierStore: ICodeVerifierStore
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val accessToken = codeVerifierStore.getAccessToken()
        return if (!isRequestWithAccessToken(response) && accessToken != null) {
            newRequestWithAccessToken(response.request, accessToken)
        } else {
            null
        }
    }

    private fun isRequestWithAccessToken(response: Response): Boolean {
        val header = response.request.header("Authorization")
        return header != null && header.startsWith("Bearer")
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}