package com.grg.spotify.data.networking

import com.grg.spotify.data.remote.SerializedUserTopItems
import com.grg.spotify.data.remote.SerializedSpotifyUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SpotifyAppService {

    @GET("v1/me/top/{type}")
    suspend fun getUsersTopItems(@Path("type") type: String): SerializedUserTopItems

    @GET("v1/me")
    suspend fun getCurrentUserProfile(): SerializedSpotifyUser

}
