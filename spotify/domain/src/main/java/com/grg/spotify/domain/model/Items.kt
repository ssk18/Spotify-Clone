package com.grg.spotify.domain.model

sealed class Item {
    data class Artist(
        val externalUrls: Map<String, String>,
        val followers: Int,
        val genres: List<String>,
        val href: String,
        val id: String,
        val images: List<Image>,
        val name: String,
        val popularity: Int,
        val type: String,
        val uri: String
    ) : Item()

    data class Track(
        val title: String,
        val album: String,
        val durationMs: Int,
        val popularity: Int
    ) : Item()
}

// Data class for image (used by Artist)
data class Image(
    val url: String,
    val height: Int,
    val width: Int
)