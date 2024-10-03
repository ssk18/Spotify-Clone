package com.grg.spotify.ui.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grg.core.domain.DataState
import com.grg.spotify.core.restartableflow.restartStateIn
import com.grg.spotify.domain.model.Artists
import com.grg.spotify.domain.model.SpotifyUser
import com.grg.spotify.domain.model.UserTopItems
import com.grg.spotify.domain.repository.ISpotifyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val spotifyDataRepository: ISpotifyDataRepository,
    @Named("default") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _topArtistsFlow = MutableStateFlow<DataState<UserTopItems>>(DataState.Loading)
    val topArtistsFlow = _topArtistsFlow
        .onStart { loadUserTopItems() }
        .restartStateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DataState.Loading
        )

    val userProfileFlow = flow<DataState<SpotifyUser>> {
        emit(DataState.Loading)
        val userProfile = spotifyDataRepository.getSpotifyUserProfile()
        userProfile.fold(
            onSuccess = { emit(DataState.Success(it)) },
            onFailure = { emit(DataState.Failure(it)) }
        )
    }.flowOn(coroutineDispatcher)
        .restartStateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = DataState.Loading
        )


    val followedArtistsInfoFlow = flow<DataState<Artists>> {
        emit(DataState.Loading)
        val followedArtists = spotifyDataRepository.getUserFollowedArtists()
        followedArtists.fold(
            onSuccess = { emit(DataState.Success(it)) },
            onFailure = { emit(DataState.Failure(it)) }
        )
    }.flowOn(coroutineDispatcher)
        .restartStateIn(
            viewModelScope,
            SharingStarted.Lazily,
            DataState.Loading
        )

    private fun loadUserTopItems() {
        viewModelScope.launch(coroutineDispatcher) {
            spotifyDataRepository.getUserTopItems("artists")
        }
    }
}