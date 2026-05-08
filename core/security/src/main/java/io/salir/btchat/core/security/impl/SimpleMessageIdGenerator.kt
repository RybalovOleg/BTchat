package io.salir.btchat.core.security.impl

import io.salir.btchat.core.security.SimpleIdGenerator
import io.salir.btchat.core.security.api.MessageIdGenerator

class SimpleMessageIdGenerator : MessageIdGenerator {

    override fun generate(): String = SimpleIdGenerator.generate(ID_LENGTH)

    companion object {
        const val ID_LENGTH = 16
    }
}