package com.grg.spotify.domain.repository

import com.grg.spotify.domain.model.SpotifyUser
import com.grg.spotify.domain.model.UserTopItems

interface ISpotifyDataRepository {

    suspend fun getUserTopItems(type: String): Result<UserTopItems>

    suspend fun getSpotifyUserProfile(): Result<SpotifyUser>
}