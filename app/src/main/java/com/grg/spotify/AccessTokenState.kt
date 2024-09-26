package com.grg.spotify

sealed class AccessTokenState {
    data object Loading : AccessTokenState()
    data object Empty : AccessTokenState()
    data class Valid(val token: String) : AccessTokenState()
}