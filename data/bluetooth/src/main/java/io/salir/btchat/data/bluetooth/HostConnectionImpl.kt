package io.salir.btchat.data.bluetooth

import android.bluetooth.BluetoothSocket
import io.salir.btchat.core.security.api.MessageSerializer
import io.salir.btchat.core.security.api.MessageIdGenerator
import io.salir.btchat.core.interfaces.Connection
import io.salir.btchat.core.model.connection.DeviceInfo
import io.salir.btchat.core.model.connection.FailedToSendMessageException
import io.salir.btchat.core.model.connection.Message
import io.salir.btchat.core.model.connection.MessageBody
import io.salir.btchat.core.security.api.MessageIdGenerator
import io.salir.btchat.core.util.TimeUtils
import io.salir.btchat.data.bluetooth.models.MessageEnvelope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

internal class HostConnectionImpl(
    override val myId: String,
    override val connectedDevices: StateFlow<Map<String, DeviceInfo>>,
    scope: CoroutineScope,
    private val sockets: StateFlow<Map<String, BluetoothSocket>>,
    private val messageSerializer: MessageSerializer,
    private val messageIdGenerator: MessageIdGenerator,
    private val onClose: () -> Unit,
) : Connection.Host {

    override val scope = CoroutineScope(SupervisorJob(scope.coroutineContext.job) + Dispatchers.IO)

    private val readerJobs = ConcurrentHashMap<String, Job>()
    private val writeLocks = ConcurrentHashMap<String, Mutex>()

    private val _messages = MutableSharedFlow<Message>(
        extraBufferCapacity = 16, onBufferOverflow = BufferOverflow.SUSPEND
    )
    override val messages: SharedFlow<Message> = _messages

    init {
        observeSockets()
    }

    private fun observeSockets() = scope.launch {
        sockets.collect { currentSockets ->
            (readerJobs.keys - currentSockets.keys).forEach { id ->
                readerJobs[id]?.cancel()
                readerJobs.remove(id)
                writeLocks.remove(id)
            }

            currentSockets.forEach { (id, socket) ->
                if (!readerJobs.containsKey(id)) {
                    readerJobs[id] = launchReader(id, socket)
                }
            }
        }
    }

    private fun launchReader(id: String, socket: BluetoothSocket): Job = scope.launch {
        try {
            val inputStream = DataInputStream(socket.inputStream)

            while (true) {
                ensureActive()

                try {
                    val size = inputStream.readInt()
                    val data = ByteArray(size)
                    inputStream.readFully(data)

                    val envelope = MessageEnvelope.from(data)

                    when (envelope.messageType) {
                        MessageEnvelope.BUSINESS_MESSAGE_TYPE -> {
                            _messages.emit(messageSerializer.deserialize(envelope.messageData))
                        }

                        MessageEnvelope.SYSTEM_MESSAGE_TYPE -> {

                        }
                    }
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    if (e is IOException) break
                }
            }
        } catch(e: Exception) {
            if (e is CancellationException) throw e
        } finally {
            readerJobs.remove(id)
        }
    }

    /**
     * @throws FailedToSendMessageException
     */
    override suspend fun sendMessageTo(
        body: MessageBody, toId: String, fromId: String?
    ): Message = withContext(Dispatchers.IO) {
        val mutex = writeLocks.getOrPut(toId) { Mutex() }

        try {
            val outputStream = DataOutputStream(sockets.value[toId]?.outputStream ?: throw NoSuchElementException())

            val message = Message(
                id = messageIdGenerator.generate(),
                fromId = fromId ?: myId,
                deliveredAt = null,
                sendedAt = TimeUtils.nowMilliseconds(),
                body = body
            )

            val envelope = MessageEnvelope(
                messageType = MessageEnvelope.BUSINESS_MESSAGE_TYPE,
                toId = toId,
                messageData = messageSerializer.serialize(message)
            )

            val data = envelope.toByteArray()

            mutex.withLock {
                outputStream.writeInt(data.size)
                outputStream.write(data)
                outputStream.flush()
            }

            message
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            throw FailedToSendMessageException()
        }
    }

    override suspend fun close() {
        onClose()
        scope.cancel()
        for ((id, socket) in sockets.value) {
            readerJobs.remove(id)?.cancel()
            writeLocks.remove(id)
            try { socket.close() } catch (_: Exception) {}
        }
    }
}