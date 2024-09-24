package com.grg.spotify.presentation.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grg.core.domain.DataState
import com.grg.core.domain.safeLet
import com.grg.core.utils.Constants
import com.grg.core.utils.Constants.AUTH_HOST
import com.grg.core.utils.Constants.AUTH_SCHEME
import com.grg.core.utils.Constants.SPOTIFY_SCOPES
import com.grg.spotify.domain.model.AccessTokenInfo
import com.grg.spotify.domain.networking.ICodeVerifierStore
import com.grg.spotify.domain.repository.IAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val codeVerifierStore: ICodeVerifierStore,
    @Named("default") private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _accessTokenFlow =
        MutableStateFlow<DataState<AccessTokenInfo>>(DataState.Uninitialized)
    val accessTokenFlow = _accessTokenFlow.onStart { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = (DataState.Uninitialized)
        )

    fun requestAuthorization(
        redirectUri: String,
        launchUrl: (url: String) -> Unit
    ) {
        _accessTokenFlow.tryEmit(DataState.Loading)
        authRepository.requestAuthorization(
            Constants.CLIENT_ID,
            scope = SPOTIFY_SCOPES,
            redirectUri = redirectUri,
            launchAuthScreen = launchUrl
        )
    }

    fun handleAuthCallbackUri(uri: Uri?, redirectUri: String) {
        uri?.let {
            if (uri.scheme == AUTH_SCHEME && uri.host == AUTH_HOST) {
                requestForAccessToken(uri, redirectUri)
            }
        }
    }

    private fun requestForAccessToken(uri: Uri, redirectUri: String) {
        val reqState =
            uri.getQueryParameter("state")?.takeIf { it == codeVerifierStore.getRequestState() }
        safeLet(uri.getQueryParameter("code"), reqState) { code, _ ->
            _accessTokenFlow.tryEmit(DataState.Loading)
            viewModelScope.launch(defaultDispatcher) {
                authRepository.postRequestAccess(
                    authCode = code,
                    codeVerifier = codeVerifierStore.getCodeVerifier().orEmpty(),
                    redirectUri = redirectUri,
                    clientId = Constants.CLIENT_ID
                ).onSuccess {
                    saveTokenInfo(it)
                    _accessTokenFlow.tryEmit(DataState.Success(it))
                }.onFailure {
                    Log.e("Access Token", "AccessToken Error", it)
                    _accessTokenFlow.tryEmit(DataState.Failure(it))
                }
            }
        } ?: run {
            _accessTokenFlow.tryEmit(DataState.Failure(Exception("Invalid access token")))
        }
        codeVerifierStore.saveRequestState(null)
    }

    private fun saveTokenInfo(tokenInfo: AccessTokenInfo?) {
        codeVerifierStore.saveAccessToken(tokenInfo?.accessToken)
        codeVerifierStore.saveRefreshToken(tokenInfo?.refreshToken)
    }
}


