package io.salir.btchat.core.model.bluetooth

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val fromId: String,
    val deliveredAt: Long,
    val body: MessageBody
)


@Serializable
sealed class MessageBody

@Serializable
data class TextMessageBody(val text: String) : MessageBody()

@Serializable
class RedirectedMessageBody(
    val toId: String,
    val messageId: String,
    val data: ByteArray
) : MessageBody()

@Serializable
class MessageStatusSentBody(
    val messageId: String
) : MessageBody()

@Serializable
class MessageStatusDeliveredBody(
    val messageId: String
) : MessageBody()

fun MessageBody.isContentMessage() = this is TextMessageBody // TODO: Сделать покрасивше