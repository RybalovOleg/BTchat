package io.salir.btchat.core.util

import kotlin.time.Clock
import kotlin.time.Instant

object TimeUtils {
    fun nowMilliseconds(): Long = Clock.System.now().toEpochMilliseconds()
}