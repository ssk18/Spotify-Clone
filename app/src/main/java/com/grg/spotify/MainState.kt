package com.grg.spotify

data class MainState(
    val isAuthSuccessful: Boolean = false,
    val accessToken: String? = null
)
