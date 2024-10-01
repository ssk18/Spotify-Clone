package com.grg.spotify.data.remote

import com.grg.spotify.domain.model.UserTopItems
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializedUserTopItems(
    @SerialName("href") val href: String,
    @SerialName("limit") val limit: Int,
    @SerialName("next") val nextPage: String? = null,
    @SerialName("previous") val prevPage: String? = null,
    @SerialName("offset") val offset: Int,
    @SerialName("total") val total: Int,
    @SerialName("items") val items: List<SerializedItem>
)