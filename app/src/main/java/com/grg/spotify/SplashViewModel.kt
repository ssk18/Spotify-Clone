package com.grg.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grg.spotify.domain.networking.ICodeVerifierStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val codeVerifierStore: ICodeVerifierStore,
    @Named("default") private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val _accessTokenFlow = MutableStateFlow<AccessTokenState>(AccessTokenState.Loading)
    val accessTokenFlow = _accessTokenFlow
        .onStart { loadAccessToken() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AccessTokenState.Loading
        )

    private fun loadAccessToken() {
        viewModelScope.launch(defaultDispatcher) {
            delay(200)
            val token = codeVerifierStore.getAccessToken()
            if (token.isNullOrEmpty()) {
                _accessTokenFlow.value = AccessTokenState.Empty
            } else {
                _accessTokenFlow.value = AccessTokenState.Valid(token)
            }
        }
    }
}