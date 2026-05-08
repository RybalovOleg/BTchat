package io.salir.btchat.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresPermission
import io.salir.btchat.core.security.api.DeviceIdentifier
import io.salir.btchat.core.model.result.Result
import io.salir.btchat.core.model.result.SimpleResult
import io.salir.btchat.core.interfaces.TransportRepository
import io.salir.btchat.core.interfaces.Connection
import io.salir.btchat.core.model.connection.DeviceInfo
import io.salir.btchat.core.model.connection.FailedToConnectException
import io.salir.btchat.core.model.connection.HandshakeFailedException
import io.salir.btchat.core.security.api.MessageIdGenerator
import io.salir.btchat.core.security.api.MessageSerializer
import io.salir.btchat.data.bluetooth.mappers.toDeviceInfo
import io.salir.btchat.data.bluetooth.models.MessageEnvelope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class BluetoothTransportRepository(
    private val context: Context,
    private val applicationScope: CoroutineScope,
    private val deviceIdentifier: DeviceIdentifier,
    private val messageSerializer: MessageSerializer,
    private val messageIdGenerator: MessageIdGenerator
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

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun scanDevices(): Flow<DeviceInfo> = callbackFlow {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }

        val receiver = object : BroadcastReceiver() {

            @RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
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
                            trySend(it.toDeviceInfo())
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

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun createHostConnection(): Unit = withContext(Dispatchers.IO) {
        _connection.value = Result.Loading.Unspecified

        try {
            val serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(BT_NAME, BT_UUID)
                ?: throw IOException()

            val connectedDevices = MutableStateFlow<Map<String, DeviceInfo>>(emptyMap())
            val sockets = MutableStateFlow<Map<String, BluetoothSocket>>(emptyMap())

            val hostConnection = HostConnectionImpl(
                myId = deviceIdentifier.me(),
                connectedDevices = connectedDevices.asStateFlow(),
                scope = applicationScope,
                sockets = sockets.asStateFlow(),
                messageSerializer = messageSerializer,
                messageIdGenerator = messageIdGenerator,
                onClose = {
                    _connection.value = Result.Empty
                    try { serverSocket.close() } catch (_: IOException) {}
                }
            )

            _connection.value = Result.Success(hostConnection)

            applicationScope.launch(Dispatchers.IO) {
                try {
                    while (true) {
                        val socket = serverSocket.accept()
                        socket?.let {
                            launch {
                                handleIncomingConnection(it, connectedDevices, sockets)
                            }
                        }
                    }
                } catch (e: IOException) {

                }
            }
        } catch (e: Exception) {
            _connection.value = Result.Error(e)
            if (e is CancellationException) throw e
        }
    }

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun handleIncomingConnection(
        socket: BluetoothSocket,
        connectedDevices: MutableStateFlow<Map<String, DeviceInfo>>,
        sockets: MutableStateFlow<Map<String, BluetoothSocket>>
    ) {
        try {
            val clientId = acceptHandshake(deviceIdentifier.me(), socket)
            val deviceInfo = socket.remoteDevice.toDeviceInfo()

            connectedDevices.value += (clientId to deviceInfo)
            sockets.value += (clientId to socket)
        } catch (e: Exception) {
            try { socket.close() } catch (_: IOException) {}
        }
    }

    override suspend fun connectTo(device: DeviceInfo): Unit = withContext(Dispatchers.IO) {
        _connection.value = Result.Loading.Unspecified

        var socket: BluetoothSocket? = null

        try {
            socket = bluetoothAdapter
                ?.getRemoteDevice(device.id)
                ?.createRfcommSocketToServiceRecord(BT_UUID)
                ?: throw FailedToConnectException()

            socket.connect()

            val me = deviceIdentifier.me()

            val hostId = makeHandshake(me, socket)

            _connection.value = Result.Success(ClientConnectionImpl(
                hostId = hostId,
                myId = me,
                scope = applicationScope,
                socket = socket,
                onClose = {
                    _connection.value = Result.Empty
                    try { socket.close() } catch (_: IOException) {}
                },
                messageSerializer = messageSerializer,
                messageIdGenerator = messageIdGenerator
            ))
        } catch (e: Exception) {
            try { socket?.close() } catch (_: IOException) {}
            _connection.value = Result.Error(e)
            if (e is CancellationException) throw e
        }
    }

    /**
     * @return Client's id
     * @throws HandshakeFailedException
     */
    private suspend fun acceptHandshake(
        me: String, socket: BluetoothSocket
    ): String = withContext(Dispatchers.IO) {
        val dataInput = DataInputStream(socket.inputStream)
        val dataOutput = DataOutputStream(socket.outputStream)

        val len = dataInput.readUnsignedByte()
        val buffer = ByteArray(len)
        dataInput.readFully(buffer)
        val clientId = buffer.decodeToString()

        val meBytes = me.toByteArray(Charsets.UTF_8)
        dataOutput.writeByte(meBytes.size)
        dataOutput.write(meBytes)
        dataOutput.flush()

        if (dataInput.readByte() != SUCCESSFUL_HANDSHAKE.toByte()) throw HandshakeFailedException()
        dataOutput.writeByte(SUCCESSFUL_HANDSHAKE)
        dataOutput.flush()

        clientId
    }

    /**
     * @return Host's id
     * @throws HandshakeFailedException
     */
    private suspend fun makeHandshake(
        me: String, socket: BluetoothSocket
    ): String = withContext(Dispatchers.IO) {
        val dataOutput = DataOutputStream(socket.outputStream)
        val dataInput = DataInputStream(socket.inputStream)

        val meBytes = me.toByteArray(Charsets.UTF_8)
        dataOutput.writeByte(meBytes.size)
        dataOutput.write(meBytes)
        dataOutput.flush()

        val len = dataInput.readUnsignedByte()
        val buffer = ByteArray(len)
        dataInput.readFully(buffer)
        val hostId = buffer.decodeToString()

        dataOutput.writeByte(SUCCESSFUL_HANDSHAKE)
        dataOutput.flush()
        if (dataInput.readByte() != SUCCESSFUL_HANDSHAKE.toByte()) throw HandshakeFailedException()

        hostId
    }

    companion object {
        const val SUCCESSFUL_HANDSHAKE = 0x33
    }
}
