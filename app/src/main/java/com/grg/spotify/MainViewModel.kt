package com.grg.spotify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grg.core.domain.DataState
import com.grg.spotify.core.restartableflow.restartStateIn
import com.grg.spotify.domain.model.SpotifyUser
import com.grg.spotify.domain.model.UserTopItems
import com.grg.spotify.domain.repository.ISpotifyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
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

    private val _userProfileDetailsFlow =
        MutableStateFlow<DataState<SpotifyUser>>(DataState.Loading)
    val userProfileDetailsFlow = _userProfileDetailsFlow
        .onStart { loadUserProfileDetails() }
        .restartStateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DataState.Loading
        )

    var state by mutableStateOf(MainState())
        private set

    private fun loadUserTopItems() {
        viewModelScope.launch(coroutineDispatcher) {
            spotifyDataRepository.getUserTopItems("artists")
        }
    }

    private fun loadUserProfileDetails() {
        viewModelScope.launch(coroutineDispatcher) {
            val response = spotifyDataRepository.getSpotifyUserProfile()
            _userProfileDetailsFlow.value = response.fold(
                onSuccess = {
                    DataState.Success(it)
                },
                onFailure = {
                    DataState.Failure(it)
                }
            )
            updateUserProfileState()
        }
    }

    private fun updateUserProfileState() {
        val newUserState = when (val userProfileState = _userProfileDetailsFlow.value) {
            is DataState.Success -> state.copy(
                userName = userProfileState.result.userName,
                isLoading = false
            )

            is DataState.Failure -> state.copy(
                error = userProfileState.throwable.message,
                isLoading = false
            )

            is DataState.Loading -> state.copy(isLoading = true)
            is DataState.Uninitialized -> state.copy(isLoading = true)
        }

        state = newUserState.copy(
            userName = newUserState.userName
        )
    }

}