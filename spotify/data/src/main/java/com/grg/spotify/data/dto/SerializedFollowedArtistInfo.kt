package com.grg.spotify.data.dto

import com.grg.spotify.data.mappers.toDomain
import com.grg.spotify.domain.model.Artists
import com.grg.spotify.domain.model.Cursor
import com.grg.spotify.domain.model.FollowedArtistInfo
import com.grg.spotify.domain.model.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SerializedArtists(
    @SerialName("artists") val artists: SerializedFollowedArtistInfo
)

@Serializable
data class SerializedFollowedArtistInfo(
    @SerialName("limit") val limit: Int,
    @SerialName("next") val next: String? = null,
    @SerialName("cursors") val cursors: SerializedCursor? = null,
    @SerialName("total") val total: Int,
    @SerialName("items") val artists: List<SerializedItem>
)

@Serializable
data class SerializedCursor(
    @SerialName("after") val after: String? = null,
    @SerialName("before") val before: String? = null
)

fun SerializedArtists.toArtist(): Artists =
    Artists(artists.toFollowedArtistInfo())

fun SerializedFollowedArtistInfo.toFollowedArtistInfo(): FollowedArtistInfo =
    FollowedArtistInfo(
        limit = limit,
        next = next,
        cursors = cursors?.toCursors(),
        total = total,
        artists = artists.filterIsInstance<SerializedItem.SerializedArtist>()
            .map { it.toDomain() }
    )


fun SerializedCursor.toCursors(): Cursor =
    Cursor(after, before)
