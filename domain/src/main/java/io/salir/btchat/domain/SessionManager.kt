package io.salir.btchat.domain

import io.salir.btchat.core.interfaces.BluetoothRepository
import io.salir.btchat.core.interfaces.Session
import io.salir.btchat.core.model.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import io.salir.btchat.core.common.Result

class SessionManager(
    private val bluetoothRepository: BluetoothRepository
) {

    private val _session = MutableStateFlow<Result<Session>>(Result.Empty)
    val session: StateFlow<Result<Session>> = _session.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<Device>>(emptyList())
    val scannedDevices: StateFlow<List<Device>> = _scannedDevices.asStateFlow()
}