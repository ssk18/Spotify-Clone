package com.grg.spotify.data.networking

import com.grg.spotify.data.remote.SerializedUserTopItems
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface SpotifyAppService {

    @GET("me/top/{type}")
    suspend fun getUsersTopItems(): ApiResponse<List<SerializedUserTopItems>>

}
