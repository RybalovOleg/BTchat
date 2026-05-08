package io.salir.btchat.data.bluetooth.models

internal typealias MessageType = Byte

internal class MessageEnvelope(
    val messageType: MessageType,
    val toId: String,
    val messageData: ByteArray
) {

    fun toByteArray(): ByteArray = byteArrayOf(messageType) + // MessageType
            byteArrayOf(toId.length.toByte()) +               // toIdSize
            toId.toByteArray(Charsets.UTF_8) +                // toId
            messageData                                       // EncodedMessage

    companion object {
        const val BUSINESS_MESSAGE_TYPE: MessageType = 0
        const val SYSTEM_MESSAGE_TYPE: MessageType = 1

        fun from(array: ByteArray): MessageEnvelope {
            val endOfId = 2 + array[1].toUByte().toInt() - 1

            return MessageEnvelope(
                messageType = array[0],
                toId = array.decodeToString(2, endOfId + 1),
                messageData = array.sliceArray(endOfId + 1..array.lastIndex)
            )
        }
    }
}


