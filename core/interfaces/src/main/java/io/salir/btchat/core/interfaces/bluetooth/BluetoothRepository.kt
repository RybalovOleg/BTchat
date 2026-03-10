package io.salir.btchat.core.interfaces.bluetooth

import io.salir.btchat.core.common.SimpleResult
import io.salir.btchat.core.model.bluetooth.DeviceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothRepository {

    val connection: StateFlow<SimpleResult<Connection>>

    suspend fun scanDevices(): Flow<DeviceInfo>

    suspend fun hostConnection()

    suspend fun connectTo(device: DeviceInfo)
}