package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.model.result.ListResult
import io.salir.btchat.core.model.result.Progress
import io.salir.btchat.core.interfaces.TransportRepository
import io.salir.btchat.core.model.connection.DeviceInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import io.salir.btchat.core.model.result.Result
import io.salir.btchat.core.model.result.SimpleResult
import io.salir.btchat.core.model.result.map
import io.salir.btchat.core.interfaces.Connection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex

class SessionManager(
    private val transportRepository: TransportRepository,
    private val scope: CoroutineScope
) {

    val session: StateFlow<SimpleResult<Session>> = transportRepository.connection.map {
        it.map { conn ->
            when (conn) {
                is Connection.Client -> ClientSession(conn)
                is Connection.Host -> HostSession(conn)
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, Result.Empty)

    private val _scannedDevices = MutableStateFlow<ListResult<DeviceInfo>>(Result.Empty)
    val scannedDevices = _scannedDevices.asStateFlow()

    private val scanMutex = Mutex()
    suspend fun scanDevices() {
        if (!scanMutex.tryLock()) return

        try {
            var result = Result.Loading<Progress.WithData<List<DeviceInfo>>>(Progress.WithData(emptyList()))
            _scannedDevices.value = result

            transportRepository.scanDevices().collect {
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
        transportRepository.createHostConnection()
    }

    suspend fun connectTo(device: DeviceInfo) {
        transportRepository.connectTo(device)
    }
}