package io.salir.btchat.core.interfaces

import io.salir.btchat.core.common.SimpleResult
import io.salir.btchat.core.model.bluetooth.Device
import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.MessageBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothRepository {

    val connection: StateFlow<SimpleResult<Connection>>

    suspend fun scanDevices(): Flow<Device>

    suspend fun hostConnection()

    suspend fun connectTo(device: Device)
}