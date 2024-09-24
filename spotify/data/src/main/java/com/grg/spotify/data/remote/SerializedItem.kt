package com.grg.spotify.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SerializedItem {

    @Serializable
    @SerialName("artist")
    data class SerializedArtist(
        @SerialName("external_urls") val externalUrls: ExternalUrls,
        @SerialName("followers") val followers: Followers,
        @SerialName("genres") val genres: List<String>,
        @SerialName("href") val href: String,
        @SerialName("id") val id: String,
        @SerialName("images") val images: List<SerializedImage>,
        @SerialName("name") val name: String,
        @SerialName("popularity") val popularity: Int,
        @SerialName("type") val type: String,
        @SerialName("uri") val uri: String
    ) : SerializedItem()

    @Serializable
    @SerialName("track")
    data class SerializedTrack(
        @SerialName("title") val title: String,
        @SerialName("album") val album: SerializedAlbum,
        @SerialName("duration_ms") val durationMs: Int,
        @SerialName("popularity") val popularity: Int
    ) : SerializedItem()
}

@Serializable
data class ExternalUrls(
    @SerialName("spotify") val spotify: String
)

@Serializable
data class SerializedAlbum(
    @SerialName("album_type") val albumType: String,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>,
)

@Serializable
data class Followers(
    @SerialName("href") val href: String?,
    @SerialName("total") val total: Int
)

@Serializable
data class SerializedImage(
    @SerialName("url") val url: String,
    @SerialName("height") val height: Int?,
    @SerialName("width") val width: Int?
)