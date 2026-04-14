package io.salir.btchat.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import io.salir.btchat.core.common.Result
import io.salir.btchat.core.common.SimpleResult
import io.salir.btchat.core.interfaces.bluetooth.TransportRepository
import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.DeviceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow

class TransportRepositoryImpl(
    private val context: Context,
    private val applicationScope: CoroutineScope
) : TransportRepository {

    val bluetoothAdapter: BluetoothAdapter? = context.getSystemService(BluetoothManager::class.java).adapter

    private val _connection = MutableStateFlow<SimpleResult<Connection>>(Result.Empty)
    override val connection: StateFlow<SimpleResult<Connection>> = _connection.asStateFlow()


    init {
        bluetoothAdapter?.let {
            if (!it.isEnabled) {

            }
        }

    }

    override suspend fun scanDevices(): Flow<DeviceInfo> = callbackFlow {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action

                when (action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice? =
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                                intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                            else
                                intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                        device?.let {
                            trySend(DeviceInfo(id = it.address))
                        }
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        this@callbackFlow.close()
                    }
                }
            }
        }

        context.registerReceiver(receiver, filter)

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }

    override suspend fun createHostConnection() {
        TODO("Not yet implemented")
    }

    override suspend fun connectTo(device: DeviceInfo) {
        TODO("Not yet implemented")
    }


}