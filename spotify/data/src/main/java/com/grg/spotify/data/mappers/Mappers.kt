package com.grg.spotify.data.mappers

import com.grg.spotify.data.dto.SerializedAlbum
import com.grg.spotify.data.dto.SerializedFollowers
import com.grg.spotify.data.dto.SerializedImage
import com.grg.spotify.data.dto.SerializedItem
import com.grg.spotify.data.dto.SerializedSpotifyUser
import com.grg.spotify.data.dto.SerializedUserTopItems
import com.grg.spotify.domain.model.Album
import com.grg.spotify.domain.model.Followers
import com.grg.spotify.domain.model.Image
import com.grg.spotify.domain.model.Item
import com.grg.spotify.domain.model.SpotifyUser
import com.grg.spotify.domain.model.UserTopItems
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

inline fun SerializedItem.toDomain(): Item {
    return when (this) {
        is SerializedItem.SerializedArtist -> Item.Artist(
            externalUrls = mapOf("spotify" to this.externalUrls.spotify),
            followers = this.followers.total,
            genres = this.genres,
            href = this.href,
            id = this.id,
            image = this.images.map { it.toImage() },
            name = this.name,
            popularity = this.popularity,
            type = this.type,
            uri = this.uri
        )

        is SerializedItem.SerializedTrack -> Item.Track(
            title = this.title,
            album = this.album.toAlbum(),
            durationMs = this.durationMs,
            popularity = this.popularity
        )
    }
}

// Mapper to convert from SerializedImage to Image (domain model)
fun SerializedImage.toImage(): Image {
    return Image(
        url = this.url,
        height = this.height!!,
        width = this.width!!
    )
}

fun SerializedAlbum.toAlbum(): Album =
    Album(
        albumType, totalTracks, availableMarkets
    )

fun SerializedFollowers.toFollowers(): Followers =
    Followers(href, total)

fun SerializedUserTopItems.toUserTopItems(): UserTopItems {
    return UserTopItems(
        href = this.href,
        limit = this.limit,
        nextPage = this.nextPage,
        prevPage = this.prevPage,
        offset = this.offset,
        total = this.total,
        item = this.items.map { it.toDomain() }
    )
}


fun SerializedSpotifyUser.toSpotifyUser(): SpotifyUser =
    SpotifyUser(
        userName = userName,
        followers = this.followers.toFollowers(),
        images = images.map { it.toImage() },
        product = product
    )


object StringSanitizer : KSerializer<String> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("UrlEncodedString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        return URLDecoder.decode(decoder.toString(), StandardCharsets.UTF_8.toString())
    }

    override fun serialize(encoder: Encoder, value: String) {
        val encoded = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
        encoder.encodeString(encoded)
    }

}