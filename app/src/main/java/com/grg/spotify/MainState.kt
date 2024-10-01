package com.grg.spotify

data class MainState(
    val userName: String = "",
    val error: String? = null,
    val topArtists: List<String> = emptyList(),
    val isLoading: Boolean = true,
)
