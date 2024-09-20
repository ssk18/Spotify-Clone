package com.grg.spotify.auth.data.mappers

import com.google.gson.annotations.SerializedName
import com.grg.spotify.domain.model.AccessTokenInfo

data class SerializedAccessTokenInfo(
    @SerializedName("access_token")
    val accessToken: String? = null,

    @SerializedName("token_type")
    val tokenType: String? = null,

    @SerializedName("scope")
    val scope: String? = null,

    @SerializedName("expires_in")
    val expiresIn: Int? = null,

    @SerializedName("refresh_token")
    val refreshToken: String? = null
)


fun AccessTokenInfo.toSerializedAccessTokenInfo(): SerializedAccessTokenInfo {
    return SerializedAccessTokenInfo(
        accessToken, tokenType, scope, expiresIn, refreshToken
    )
}

fun SerializedAccessTokenInfo.toAccessTokenInfo(): AccessTokenInfo {
    return AccessTokenInfo(
        accessToken = accessToken,
        tokenType = tokenType,
        scope = scope,
        refreshToken = refreshToken,
        expiresIn = expiresIn
    )
}
