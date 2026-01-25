package io.salir.btchat.core.common

abstract class Progress {

    object Unspecified : Progress()

    data class WithData<out T>(val data: T) : Progress()
}