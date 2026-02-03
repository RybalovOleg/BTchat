package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.MessageBody
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.time.Clock

internal class ClientSession(
    private val conn: Connection.Client,
) : Session {

    private val _messages = MutableSharedFlow<Message>()
    override val messages: SharedFlow<Message> = _messages.asSharedFlow()

    override suspend fun sendMessage(body: MessageBody) {
        conn.sendMessage(Message(
            from = conn.thisDevice,
            timestamp = Clock.System.now(),
            body = body,
        ))
    }
}