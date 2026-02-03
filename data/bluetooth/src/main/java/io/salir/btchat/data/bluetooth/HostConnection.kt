package io.salir.btchat.data.bluetooth

import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.Device
import io.salir.btchat.core.model.bluetooth.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class HostConnection(
    override val thisDevice: Device,
    override val scope: CoroutineScope
) : Connection.Host {

    private val _messages = MutableSharedFlow<Message>()
    override val messages = _messages.asSharedFlow()

    private val _connectedDevices = MutableStateFlow<Set<Device>>(emptySet())
    override val connectedDevices = _connectedDevices.asStateFlow()


    override suspend fun sendMessageTo(message: Message, to: Device) {
        TODO("Not yet implemented")
    }

    override suspend fun close() {
        TODO("Not yet implemented")
    }
}