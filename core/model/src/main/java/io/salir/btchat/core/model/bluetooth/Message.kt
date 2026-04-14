package io.salir.btchat.core.model.bluetooth

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val fromId: String,
    val timestamp: Long,
    val body: MessageBody
)


@Serializable
sealed class MessageBody {

    @Serializable
    data class TextMessage(val text: String) : MessageBody()
}