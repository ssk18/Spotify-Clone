package com.grg.core.utils

object Constants {

    const val CLIENT_ID = "a7d34ca23db84e71b1cf200412af50d1"
    const val SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize"
    const val Spotify_BASE_URL = "https://api.spotify.com/"
    const val BASE_URL = "https://accounts.spotify.com/"
    const val AUTH_HOST = "com.myspotify.authorization"
    const val AUTH_SCHEME = "gssa"
    const val REDIRECT_URI   = "$AUTH_SCHEME://$AUTH_HOST"
    val DEFAULT_TIMEOUT_SECONDS = 10000L
    val SPOTIFY_SCOPES = listOf("user-read-private","user-read-email","user-top-read","user-follow-read").joinToString(separator = " ")

}