package com.grg.spotify.presentation.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grg.spotify.auth.data.utils.Constants
import com.grg.spotify.domain.IAuthRepository
import com.grg.spotify.domain.ICodeVerifierStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val codeVerifierStore: ICodeVerifierStore,
    @Named("default") private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun requestAuthorization(
        redirectUri: String,
        launchUrl: (url: String) -> Unit
    ) {
        authRepository.requestAuthorization(
            Constants.CLIENT_ID,
            scope = "user-read-private user-read-email",
            redirectUri = redirectUri,
            launchAuthScreen = launchUrl
        )
    }

    fun handleAuthCallbackUri(uri: Uri, redirectUri: String) {
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        if (codeVerifierStore.getRequestState() == state) {
            viewModelScope.launch(defaultDispatcher) {
                authRepository.postRequestAccess(
                    authCode = code.orEmpty(),
                    codeVerifier = codeVerifierStore.getCodeVerifier().orEmpty(),
                    redirectUri = redirectUri,
                    clientId = Constants.CLIENT_ID
                )
            }
        }
        codeVerifierStore.saveRequestState(null)
    }
}