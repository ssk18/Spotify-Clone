package com.grg.spotify.data.mappers

import com.grg.spotify.data.remote.SerializedImage
import com.grg.spotify.data.remote.SerializedItem
import com.grg.spotify.data.remote.SerializedUserTopItems
import com.grg.spotify.domain.model.Image
import com.grg.spotify.domain.model.Item
import com.grg.spotify.domain.model.UserTopItems

fun SerializedItem.toDomain(): Item {
    return when (this) {
        is SerializedItem.SerializedArtist -> Item.Artist(
            externalUrls = mapOf("spotify" to this.externalUrls.spotify),
            followers = this.followers.total,
            genres = this.genres,
            href = this.href,
            id = this.id,
            images = this.images.map { it.toDomain() },
            name = this.name,
            popularity = this.popularity,
            type = this.type,
            uri = this.uri
        )
        is SerializedItem.SerializedTrack -> Item.Track(
            title = this.title,
            album = this.album,
            durationMs = this.durationMs,
            popularity = this.popularity
        )
    }
}

// Mapper to convert from SerializedImage to Image (domain model)
fun SerializedImage.toDomain(): Image {
    return Image(
        url = this.url,
        height = this.height,
        width = this.width
    )
}

fun SerializedUserTopItems.toDomain(): UserTopItems {
    return UserTopItems(
        href = this.href,
        limit = this.limit,
        nextPage = this.nextPage,
        prevPage = this.prevPage,
        offset = this.offset,
        total = this.total,
        item = this.item.toDomain()
    )
}