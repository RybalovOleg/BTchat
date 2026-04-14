package io.salir.btchat.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.content.Context
import androidx.annotation.RequiresPermission
import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.DeviceInfo
import io.salir.btchat.core.model.bluetooth.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

//@SuppressLint("MissingPermission")
//internal class HostConnection
//@RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT) constructor(
//    private val scope: CoroutineScope,
//    private val context: Context,
//    private val bluetoothAdapter: BluetoothAdapter
//) : Connection.Host {
//
////    override val thisDevice: DeviceInfo = bluetoothAdapter.address
//
//    private val _messages = MutableSharedFlow<Message>()
//    override val messages = _messages.asSharedFlow()
//
//    private val _connectedDevices = MutableStateFlow<Set<DeviceInfo>>(emptySet())
//    override val connectedDevices = _connectedDevices.asStateFlow()
//
//    private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
//        bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(BT_NAME, BT_UUID)
//    }
//
//
//    override suspend fun sendMessageTo(message: Message, to: DeviceInfo) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun close() {
//        TODO("Not yet implemented")
//    }
//}