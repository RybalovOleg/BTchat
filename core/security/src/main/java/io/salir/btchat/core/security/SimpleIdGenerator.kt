package io.salir.btchat.core.security

import java.security.SecureRandom

internal object SimpleIdGenerator {

    private val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun generate(length: Int): String {
        val sb = StringBuilder(length)

        val bytes = ByteArray(length)
        SecureRandom().nextBytes(bytes)

        bytes.forEach { sb.append(chars[it.toInt() and 0x3D]) }

        return sb.toString()
    }
}