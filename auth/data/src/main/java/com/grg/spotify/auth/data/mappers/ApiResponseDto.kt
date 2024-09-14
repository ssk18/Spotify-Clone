package com.grg.spotify.auth.data.mappers

import com.grg.spotify.core.extensions.orElse
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