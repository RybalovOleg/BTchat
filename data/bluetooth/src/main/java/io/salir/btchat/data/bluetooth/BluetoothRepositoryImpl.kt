package io.salir.btchat.data.bluetooth

import io.salir.btchat.core.common.Result
import io.salir.btchat.core.common.SimpleResult
import io.salir.btchat.core.interfaces.bluetooth.BluetoothRepository
import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BluetoothRepositoryImpl : BluetoothRepository {

    private val _connection = MutableStateFlow<SimpleResult<Connection>>(Result.Empty)
    override val connection = _connection.asStateFlow()

    override suspend fun scanDevices(): Flow<Device> {
        TODO("Not yet implemented")
    }

    override suspend fun hostConnection() {
        TODO("Not yet implemented")
    }

    override suspend fun connectTo(device: Device) {
        TODO("Not yet implemented")
    }


}