package io.salir.btchat.core.interfaces

import io.salir.btchat.core.model.connection.DeviceInfo
import io.salir.btchat.core.model.connection.Message
import io.salir.btchat.core.model.connection.MessageBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface Connection {

    val scope: CoroutineScope
    val myId: String
    val messages: SharedFlow<Message>

    suspend fun close()


    interface Client : Connection {
        val hostId: String
        val hostInfo: DeviceInfo

        suspend fun sendMessageTo(body: MessageBody, toId: String): Message
    }

    interface Host : Connection {
        val connectedDevices: StateFlow<Map<String, DeviceInfo>>

        suspend fun sendMessageTo(body: MessageBody, toId: String, fromId: String? = null): Message
    }
}