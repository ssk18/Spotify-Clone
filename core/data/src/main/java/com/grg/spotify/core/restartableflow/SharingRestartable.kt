package com.grg.spotify.core.restartableflow

import kotlinx.coroutines.flow.SharingStarted

interface SharingRestartable: SharingStarted {
    fun restart()
}