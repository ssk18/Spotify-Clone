package com.grg.spotify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grg.core.domain.DataState
import com.grg.spotify.domain.model.AccessTokenInfo
import com.grg.spotify.domain.networking.ICodeVerifierStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val codeVerifierStore: ICodeVerifierStore
) : ViewModel() {

    // StateFlow to hold the access token state
    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    val accessTokenFlow = _accessTokenFlow.onStart { loadAccessToken() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ""
        )

    // Function to load the access token asynchronously
    private fun loadAccessToken() {
        viewModelScope.launch {
            val token = codeVerifierStore.getAccessToken()
            _accessTokenFlow.value = token
        }
    }
}