package com.grg.spotify.auth.data.utils

import kotlin.time.Duration.Companion.seconds

object Constants {

    const val CLIENT_ID = "a7d34ca23db84e71b1cf200412af50d1"
    const val SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize"
    const val BASE_URL = "https://accounts.spotify.com/"
    const val AUTH_HOST = "com.myspotify.authorization"
    const val AUTH_SCHEME = "gssa"
    const val REDIRECT_URI   = "${AUTH_SCHEME}://${AUTH_HOST}"
    val DEFAULT_TIMEOUT_SECONDS = 10000L

}