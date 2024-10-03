package com.grg.spotify.data.networking

import com.grg.spotify.data.dto.SerializedArtists
import com.grg.spotify.data.dto.SerializedFollowedArtistInfo
import com.grg.spotify.data.dto.SerializedUserTopItems
import com.grg.spotify.data.dto.SerializedSpotifyUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyAppService {

    @GET("v1/me/top/{type}")
    suspend fun getUsersTopItems(@Path("type") type: String): SerializedUserTopItems

    @GET("v1/me")
    suspend fun getCurrentUserProfile(): SerializedSpotifyUser

    @GET("v1/me/following")
    suspend fun getUserFollowedArtists(
        @Query("type") type: String = "artist"
    ): SerializedArtists

}
