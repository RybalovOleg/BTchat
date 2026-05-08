package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.model.connection.Message
import io.salir.btchat.core.model.connection.MessageBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

sealed class Session {

    abstract val scope: CoroutineScope

    abstract val myId: String

    abstract val messages: SharedFlow<Message>

    abstract suspend fun sendMessage(body: MessageBody, toId: String)

    abstract suspend fun close()
}