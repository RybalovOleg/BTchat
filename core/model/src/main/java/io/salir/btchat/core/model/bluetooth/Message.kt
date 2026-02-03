package io.salir.btchat.core.model.bluetooth

import kotlin.time.Instant

data class Message(
    val from: Device,
    val timestamp: Instant,
    val body: MessageBody
)

sealed class MessageBody {

    data class Text(val text: String) : MessageBody()

    data object GetConnectedDevicesList : MessageBody()
    data class ConnecntedDevicesList(val devices: List<Device>) : MessageBody()
}