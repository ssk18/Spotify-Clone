package com.grg.spotify.domain

data class AccessTokenInfo(
    val accessToken: String,
    val tokenType: String,
    val scope: String,
    val expiresIn: Int,
    val refreshToken: String
)
