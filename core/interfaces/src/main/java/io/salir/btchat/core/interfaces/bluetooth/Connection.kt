package io.salir.btchat.core.interfaces.bluetooth

import io.salir.btchat.core.model.bluetooth.Device
import io.salir.btchat.core.model.bluetooth.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface Connection {

    val messages: SharedFlow<Message>
    val thisDevice: Device

    val scope: CoroutineScope

    suspend fun close()


    interface Client : Connection {
        val host: Device

        suspend fun sendMessage(message: Message)
    }

    interface Host : Connection {
        val connectedDevices: StateFlow<Set<Device>>

        suspend fun sendMessageTo(message: Message, to: Device)
    }
}