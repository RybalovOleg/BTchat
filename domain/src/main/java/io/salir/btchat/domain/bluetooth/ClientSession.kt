package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.MessageBody
import io.salir.btchat.core.model.bluetooth.Peer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.time.Clock

internal class ClientSession(
    private val conn: Connection.Client
) : Session() {

    override val me: Peer
        get() = conn.me

    private val _messages = MutableSharedFlow<Message>()
    override val messages: SharedFlow<Message> = _messages.asSharedFlow()

    override suspend fun close() {
        conn.close()
    }

    override suspend fun sendMessage(body: MessageBody, toId: String) {
//        conn.sendMessage(Message(
//            from = conn.thisDevice,
//            timestamp = Clock.System.now(),
//            body = body,
//        ))
    }
}