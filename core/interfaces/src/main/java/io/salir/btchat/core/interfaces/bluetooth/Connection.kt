package io.salir.btchat.core.interfaces.bluetooth

import io.salir.btchat.core.model.bluetooth.DeviceInfo
import io.salir.btchat.core.model.bluetooth.Message
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface Connection {

    val messages: SharedFlow<Message>

    suspend fun close()


    interface Client : Connection {
        val host: DeviceInfo

        suspend fun sendMessage(message: Message)
    }

    interface Host : Connection {
        val connectedDevices: StateFlow<Set<DeviceInfo>>

        suspend fun sendMessageTo(message: Message, to: DeviceInfo)
    }
}