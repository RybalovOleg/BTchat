package io.salir.btchat.domain

import io.salir.btchat.core.interfaces.Session
import io.salir.btchat.core.model.Message
import io.salir.btchat.core.model.MessageBody
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ClientSession : Session {

    private val _messages = MutableSharedFlow<Message>()
    override val messages: SharedFlow<Message> = _messages.asSharedFlow()

    override suspend fun sendMessage(message: MessageBody) {
        TODO("Not yet implemented")
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }
}