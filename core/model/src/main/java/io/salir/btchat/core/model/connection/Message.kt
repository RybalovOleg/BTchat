package io.salir.btchat.core.model.connection

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val fromId: String,
    val deliveredAt: Long? = null,
    val sendedAt: Long,
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
class MessageStatusDeliveredBody(
    val messageId: String,
    val timestap: Long
) : MessageBody()

fun Message.isContentMessage() = body.isContentBody()
fun MessageBody.isContentBody() = this is TextMessageBody // TODO: Сделать покрасивше
