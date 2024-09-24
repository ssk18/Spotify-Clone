package com.grg.spotify.domain.model

data class UserTopItems(
    val href: String,
    val limit: Int,
    val nextPage: String? = null,
    val prevPage: String? = null,
    val offset: Int,
    val total: Int,
    val item: List<Item>,
)
