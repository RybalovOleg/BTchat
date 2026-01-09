package io.salir.btchat.core.model

import kotlin.time.Instant

data class Message(
    val fromID: String,
    val timestamp: Instant,
    val body: MessageBody
)

sealed class MessageBody {

    data class Text(val text: String) : MessageBody()
}