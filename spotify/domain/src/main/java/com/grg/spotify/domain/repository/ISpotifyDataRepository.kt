package com.grg.spotify.domain.repository

import com.grg.spotify.domain.model.Artists
import com.grg.spotify.domain.model.FollowedArtistInfo
import com.grg.spotify.domain.model.SpotifyUser
import com.grg.spotify.domain.model.UserTopItems

interface ISpotifyDataRepository {

    suspend fun getUserTopItems(type: String): Result<UserTopItems>

    suspend fun getSpotifyUserProfile(): Result<SpotifyUser>

    suspend fun getUserFollowedArtists(): Result<Artists>
}