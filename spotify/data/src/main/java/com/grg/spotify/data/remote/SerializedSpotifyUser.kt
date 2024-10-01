package com.grg.spotify.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializedSpotifyUser(
    @SerialName("display_name") val userName: String,
    @SerialName("followers") val followers: SerializedFollowers,
    @SerialName("images") val images: List<SerializedImage>,
    @SerialName("product") val product: String
)