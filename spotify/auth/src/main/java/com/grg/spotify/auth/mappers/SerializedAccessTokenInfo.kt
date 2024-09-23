package com.grg.spotify.auth.mappers

import com.grg.spotify.domain.model.AccessTokenInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializedAccessTokenInfo(
    @SerialName("access_token")
    val accessToken: String? = null,

    @SerialName("token_type")
    val tokenType: String? = null,

    @SerialName("scope")
    val scope: String? = null,

    @SerialName("expires_in")
    val expiresIn: Int? = null,

    @SerialName("refresh_token")
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
