package com.grg.spotify.data.networking

import android.util.Log
import com.grg.core.utils.Constants
import com.grg.spotify.domain.networking.ICodeVerifierStore
import com.grg.spotify.domain.repository.IAuthRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.UUID
import javax.inject.Inject

class AccessTokenAuthenticator @Inject constructor(
    private val codeVerifierStore: ICodeVerifierStore,
    private val spotifyAuthRepo: IAuthRepository
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        val buildNewRequest: (String) -> Request = {
            response.request.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        }
        return runBlocking {
            val hasLockBefore = AUTHENTICATOR_LOCK.isLocked
            val lockKey = UUID.randomUUID().toString()
            AUTHENTICATOR_LOCK.lock(lockKey)
            val request = if (hasLockBefore) {
                // Some one just refreshed access token
                buildNewRequest(codeVerifierStore.getAccessToken().orEmpty())
            } else {
                spotifyAuthRepo.refreshToken(
                    refreshToken = codeVerifierStore.getRefreshToken().orEmpty(),
                    clientId = Constants.CLIENT_ID
                ).fold({ accessTokenInfo ->
                    Log.d(javaClass.simpleName, "Got access token: $accessTokenInfo")
                    codeVerifierStore.saveAccessToken(accessTokenInfo.accessToken)
                    codeVerifierStore.saveRefreshToken(accessTokenInfo.refreshToken)
                    buildNewRequest(accessTokenInfo.accessToken.orEmpty())
                }, {
                    Log.e(javaClass.simpleName, "Error refreshing access token", it)
                    null
                })
            }
            AUTHENTICATOR_LOCK.unlock(lockKey)
            request
        }
    }

    companion object {
        private val AUTHENTICATOR_LOCK = Mutex()
    }
}