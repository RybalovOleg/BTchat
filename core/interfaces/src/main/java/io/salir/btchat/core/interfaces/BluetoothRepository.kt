package io.salir.btchat.core.interfaces

import io.salir.btchat.core.model.Device
import io.salir.btchat.core.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothRepository {

    val session: StateFlow<Result<Session>>

    suspend fun scanDevices(): Flow<Device>

    suspend fun hostNewSession()

    suspend fun connectToDevice(device: Device)

    suspend fun sendMessage(message: Message)

    suspend fun stopSession()
}