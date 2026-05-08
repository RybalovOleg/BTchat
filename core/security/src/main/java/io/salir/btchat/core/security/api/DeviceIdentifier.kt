package io.salir.btchat.core.security.api

interface DeviceIdentifier {
    suspend fun me(): String
}