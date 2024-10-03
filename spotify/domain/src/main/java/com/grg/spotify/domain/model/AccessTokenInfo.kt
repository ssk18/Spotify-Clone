package com.grg.spotify.domain.model

data class AccessTokenInfo(
    val accessToken: String? = null,
    val tokenType: String? = null,
    val scope: String? = null,
    val expiresIn: Int? = null,
    val refreshToken: String? = null
)


