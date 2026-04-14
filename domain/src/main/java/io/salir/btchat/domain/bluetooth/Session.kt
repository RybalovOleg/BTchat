package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.MessageBody
import io.salir.btchat.core.model.bluetooth.Peer
import kotlinx.coroutines.flow.SharedFlow

sealed class Session {

    abstract val me: Peer

    abstract val messages: SharedFlow<Message>

    abstract suspend fun sendMessage(body: MessageBody, toId: String)

    abstract suspend fun close()
}