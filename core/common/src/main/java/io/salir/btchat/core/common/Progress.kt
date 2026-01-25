package io.salir.btchat.core.common

abstract class Progress {

    object Unspecified : Progress()

    data class WithData<T>(val data: T) : Progress()
}