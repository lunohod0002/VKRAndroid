package com.example.vkr.network.api

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object AuthEvents {
    private val _logoutEvents = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val logoutEvents: SharedFlow<Unit> = _logoutEvents

    fun emitLogout() { _logoutEvents.tryEmit(Unit) }
}