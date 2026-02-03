package io.salir.btchat.data.bluetooth

import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.Device
import io.salir.btchat.core.model.bluetooth.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ClientConnection(
    override val thisDevice: Device,
    override val host: Device,
    override val scope: CoroutineScope
) : Connection.Client {

    private val _messages = MutableSharedFlow<Message>()
    override val messages = _messages.asSharedFlow()


    override suspend fun sendMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }
}