package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.MessageBody
import kotlinx.coroutines.flow.SharedFlow

interface Session {

    val messages: SharedFlow<Message>

    suspend fun sendMessage(body: MessageBody)
}