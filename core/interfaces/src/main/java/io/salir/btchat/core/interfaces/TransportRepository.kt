package io.salir.btchat.core.interfaces

import io.salir.btchat.core.model.result.SimpleResult
import io.salir.btchat.core.model.connection.DeviceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TransportRepository {

    val connection: StateFlow<SimpleResult<Connection>>

    suspend fun scanDevices(): Flow<DeviceInfo>

    suspend fun createHostConnection()

    suspend fun connectTo(device: DeviceInfo)
}