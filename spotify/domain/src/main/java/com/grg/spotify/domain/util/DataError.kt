package com.grg.spotify.domain.util

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    sealed class ServerError(val message: String) : DataError {
        data object DiskFull : ServerError("Unable to reach server")
        class CustomServerError(message: String) : ServerError(message)
    }
}