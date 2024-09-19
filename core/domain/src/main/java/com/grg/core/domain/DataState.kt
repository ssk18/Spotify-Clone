package com.grg.core.domain

sealed interface DataState<out R> {
    data object Loading : DataState<Nothing>
    data class Success<out R>(val result: R) : DataState<R>
    data class Failure(val throwable: Throwable) : DataState<Nothing>
    data object Uninitialized : DataState<Nothing>
}