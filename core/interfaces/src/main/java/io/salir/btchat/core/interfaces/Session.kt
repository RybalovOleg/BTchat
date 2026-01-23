package io.salir.btchat.core.interfaces

import io.salir.btchat.core.model.Message
import io.salir.btchat.core.model.MessageBody
import kotlinx.coroutines.flow.SharedFlow

interface Session {

    val messages: SharedFlow<Message>

    suspend fun sendMessage(message: MessageBody)
}