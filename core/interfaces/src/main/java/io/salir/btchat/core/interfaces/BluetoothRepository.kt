package io.salir.btchat.core.interfaces

import io.salir.btchat.core.common.Progress
import io.salir.btchat.core.common.Result
import io.salir.btchat.core.model.bluetooth.Device
import io.salir.btchat.core.model.bluetooth.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothRepository {

    val session: StateFlow<Result<Session, Progress.Unspecified>>

    suspend fun scanDevices(): Flow<Device>

    suspend fun hostNewSession()

    suspend fun connectToSession(device: Device)

    suspend fun sendMessage(message: Message)

    suspend fun stopSession()
}