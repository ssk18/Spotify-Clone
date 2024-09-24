package com.grg.spotify.auth.model

import com.grg.core.domain.orElse
import com.grg.spotify.domain.model.AccessTokenInfo
import com.skydoves.sandwich.ApiResponse


inline fun <reified T> ApiResponse<T>.toResult(): Result<T> {
    return when (this) {
        is ApiResponse.Success -> Result.success(data)
        is ApiResponse.Failure.Error -> Result.failure(
            Exception(
                payload?.toString().orElse("ApiResponse.Failure.Error")
            )
        )

        is ApiResponse.Failure.Exception -> Result.failure(throwable)
    }
}

fun ApiResponse<SerializedAccessTokenInfo>.toDomain(): Result<AccessTokenInfo> {
    return when (this) {
        is ApiResponse.Success -> {
            // Map SerializedAccessTokenInfo to domain model AccessTokenInfo
            Result.success(this.data.toAccessTokenInfo())
        }
        is ApiResponse.Failure.Exception -> {
            // Return the exception wrapped in Result
            Result.failure(this.throwable)
        }
        is ApiResponse.Failure.Error -> {
            // Handle API-specific error
            Result.failure(Exception("API Error: ${this.payload}"))
        }
    }
}
