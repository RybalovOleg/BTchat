package io.salir.btchat.core.interfaces.bluetooth

import io.salir.btchat.core.model.bluetooth.DeviceInfo
import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.Peer
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface Connection {

    val me: Peer

    val messages: SharedFlow<Message>

    suspend fun sendMessageTo(message: Message, to: DeviceInfo)

    suspend fun close()


    interface Client : Connection {
        val host: Peer
        val hostInfo: DeviceInfo
    }

    interface Host : Connection {
        val connectedDevices: StateFlow<Map<Peer, DeviceInfo>>
    }
}