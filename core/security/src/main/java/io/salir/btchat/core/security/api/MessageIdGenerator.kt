package io.salir.btchat.core.security.api

fun interface MessageIdGenerator {
    fun generate(): String
}