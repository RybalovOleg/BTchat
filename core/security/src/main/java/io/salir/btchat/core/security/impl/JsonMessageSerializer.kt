package io.salir.btchat.core.security.impl

import io.salir.btchat.core.security.api.MessageSerializer
import io.salir.btchat.core.model.connection.Message
import kotlinx.serialization.json.Json

internal class JsonMessageSerializer : MessageSerializer {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    override fun serialize(message: Message): ByteArray = json
        .encodeToString(message)
        .toByteArray(Charsets.UTF_8)

    override fun deserialize(bytes: ByteArray): Message = json
        .decodeFromString(bytes.decodeToString())
}