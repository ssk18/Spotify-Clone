package com.grg.spotify.domain.model

data class SpotifyUser(
    val userName: String,
    val followers: Followers,
    val images: List<Image>,
    val product: String
)

data class Followers(
    val href: String?,
    val total: Int
)