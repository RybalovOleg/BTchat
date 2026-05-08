package io.salir.btchat.core.security.api

import io.salir.btchat.core.model.connection.Message

interface MessageSerializer {

    fun serialize(message: Message): ByteArray

    fun deserialize(bytes: ByteArray): Message
}