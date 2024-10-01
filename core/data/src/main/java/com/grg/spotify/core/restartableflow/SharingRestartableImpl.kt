package com.grg.spotify.core.restartableflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class SharingRestartableImpl(
    private val sharingStarted: SharingStarted
) : SharingRestartable {

    private val restartFlow = MutableSharedFlow<SharingCommand>(extraBufferCapacity = 2)

    override fun restart() {
        restartFlow.tryEmit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE)
        restartFlow.tryEmit(SharingCommand.START)
    }

    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> {
        return merge(restartFlow, sharingStarted.command(subscriptionCount))
    }
}

fun SharingStarted.restartFlow(): SharingRestartable {
    return SharingRestartableImpl(this)
}

interface RestartSharedFlow<out T> : SharedFlow<T> {
    fun restart()
}

interface RestartStateFlow<out T> : StateFlow<T> {
    fun restart()
}

fun <T> Flow<T>.restartShareIn(
    scope: CoroutineScope,
    started: SharingStarted,
): RestartSharedFlow<T> {
    val sharingRestartable = started.restartFlow()
    val sharedFlow = shareIn(scope, sharingRestartable)
    return object : RestartSharedFlow<T>, SharedFlow<T> by sharedFlow {
        override fun restart() =
            sharingRestartable.restart()
    }
}

fun <T> Flow<T>.restartStateIn(
    scope: CoroutineScope,
    started: SharingStarted,
    initialValue: T
): RestartStateFlow<T> {
    val sharingRestartable = started.restartFlow()
    val stateFlow = stateIn(scope, sharingRestartable, initialValue)
    return object : RestartStateFlow<T>, StateFlow<T> by stateFlow {
        override fun restart() =
            sharingRestartable.restart()
    }
}



