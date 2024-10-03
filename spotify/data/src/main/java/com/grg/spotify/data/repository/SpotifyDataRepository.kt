package com.grg.spotify.data.repository

import android.util.Log
import com.grg.spotify.data.dto.toArtist
import com.grg.spotify.data.mappers.toSpotifyUser
import com.grg.spotify.data.mappers.toUserTopItems
import com.grg.spotify.data.networking.SpotifyAppService
import com.grg.spotify.domain.model.Artists
import com.grg.spotify.domain.model.SpotifyUser
import com.grg.spotify.domain.model.UserTopItems
import com.grg.spotify.domain.repository.ISpotifyDataRepository
import javax.inject.Inject

class SpotifyDataRepository @Inject constructor(
    private val spotifyAppService: SpotifyAppService
) : ISpotifyDataRepository {

    override suspend fun getUserTopItems(type: String): Result<UserTopItems> {
        return runCatching {
            spotifyAppService.getUsersTopItems(type).toUserTopItems()
        }.onFailure {
            Log.e("SpotifyDataRepository","SpotifyDataRepository", it)
        }
    }

    override suspend fun getSpotifyUserProfile(): Result<SpotifyUser> {
        return runCatching {
            spotifyAppService.getCurrentUserProfile().toSpotifyUser()
        }
    }

    override suspend fun getUserFollowedArtists(): Result<Artists> {
        return runCatching {
            spotifyAppService.getUserFollowedArtists().toArtist()
        }
    }

}