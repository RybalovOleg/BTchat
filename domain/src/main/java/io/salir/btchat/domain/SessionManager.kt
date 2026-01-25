package io.salir.btchat.domain

import io.salir.btchat.core.common.ListResult
import io.salir.btchat.core.common.Progress
import io.salir.btchat.core.interfaces.BluetoothRepository
import io.salir.btchat.core.interfaces.Session
import io.salir.btchat.core.model.bluetooth.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import io.salir.btchat.core.common.Result
import io.salir.btchat.core.common.SimpleResult
import kotlinx.coroutines.sync.Mutex

class SessionManager(
    private val bluetoothRepository: BluetoothRepository
) {

    val session: StateFlow<SimpleResult<Session>> = bluetoothRepository.session

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
}