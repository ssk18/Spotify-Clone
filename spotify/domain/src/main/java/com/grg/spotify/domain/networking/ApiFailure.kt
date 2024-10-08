package com.grg.spotify.domain.networking

sealed interface ApiFailure {
    data class ApiError(val payload: Any?) : ApiFailure
    data class ApiException(val throwable: Throwable) : ApiFailure
}