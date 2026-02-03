package io.salir.btchat.domain

import io.salir.btchat.core.common.ListResult
import io.salir.btchat.core.common.Progress
import io.salir.btchat.core.interfaces.BluetoothRepository
import io.salir.btchat.domain.Session
import io.salir.btchat.core.model.bluetooth.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import io.salir.btchat.core.common.Result
import io.salir.btchat.core.common.SimpleResult
import io.salir.btchat.core.common.map
import io.salir.btchat.core.interfaces.Connection
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex

class SessionManager(
    private val bluetoothRepository: BluetoothRepository
) {

    val session: StateFlow<SimpleResult<Session>> = bluetoothRepository.connection.map {
        it.map { conn ->
            when (conn) {
                is Connection.Client -> ClientSession(conn)
                is Connection.Host -> HostSession(conn)
            }
        }
    } as StateFlow<SimpleResult<Session>>

    private val _scannedDevices = MutableStateFlow<ListResult<Device>>(Result.Empty)
    val scannedDevices = _scannedDevices.asStateFlow()

    private val scanMutex = Mutex()

    suspend fun scanDevices() {
        if (!scanMutex.tryLock()) return

        try {
            var result = Result.Loading<Progress.WithData<List<Device>>>(Progress.WithData(emptyList()))
            _scannedDevices.value = result

            bluetoothRepository.scanDevices().collect {
                result = Result.Loading(
                    Progress.WithData(result.progress.data + it)
                )
                _scannedDevices.value = result
            }

            _scannedDevices.value = Result.Success(result.progress.data)
        } catch (e: Exception) {
            _scannedDevices.value = Result.Error(e)
        } finally {
            scanMutex.unlock()
        }
    }

    suspend fun hostNewSession() {
        bluetoothRepository.hostConnection()
    }

    suspend fun connectToSession(device: Device) {
        bluetoothRepository.connectTo(device)
    }
}