package com.grg.spotify.data.networking

import com.grg.spotify.domain.networking.ICodeVerifierStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(private val codeVerifierStore: ICodeVerifierStore) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request().newBuilder()
            .header("Authorization", "Bearer ${codeVerifierStore.getAccessToken().orEmpty()}")
            .build().let {
                chain.proceed(it)
            }
    }
}