package com.grg.spotify.domain.model

data class Artists(
    val artist: FollowedArtistInfo
)

data class FollowedArtistInfo(
    val limit: Int,
    val next: String?,
    val cursors: Cursor?,
    val total: Int,
    val artists: List<Item>
)

data class Cursor(
    val after: String?,
    val before: String?
)