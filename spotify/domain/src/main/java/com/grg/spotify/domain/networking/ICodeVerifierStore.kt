package com.grg.spotify.domain.networking

interface ICodeVerifierStore {

    fun getCodeVerifier(): String?

    fun saveCodeVerifier(codeVerifier: String?)

    fun saveRequestState(state: String?)

    fun getRequestState(): String?

    fun getAccessToken(): String?

    fun saveAccessToken(accessToken: String?)

    fun getRefreshToken(): String?

    fun saveRefreshToken(refreshToken: String?)

}