package io.salir.btchat.data.bluetooth

import android.bluetooth.BluetoothSocket
import io.salir.btchat.core.security.api.MessageSerializer
import io.salir.btchat.core.interfaces.Connection
import io.salir.btchat.core.model.connection.DeviceInfo
import io.salir.btchat.core.model.connection.FailedToSendMessageException
import io.salir.btchat.core.model.connection.Message
import io.salir.btchat.core.model.connection.MessageBody
import io.salir.btchat.core.security.api.MessageIdGenerator
import io.salir.btchat.core.util.TimeUtils
import io.salir.btchat.data.bluetooth.mappers.toDeviceInfo
import io.salir.btchat.data.bluetooth.models.MessageEnvelope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

internal class ClientConnectionImpl(
    override val hostId: String,
    override val myId: String,
    override val scope: CoroutineScope,
    private val socket: BluetoothSocket,
    private val messageSerializer: MessageSerializer,
    private val messageIdGenerator: MessageIdGenerator,
    private val onClose: () -> Unit
): Connection.Client {

    override val hostInfo: DeviceInfo = socket.remoteDevice.toDeviceInfo()

    private val inputStream = DataInputStream(socket.inputStream)
    private val outputStream = DataOutputStream(socket.outputStream)

    private val _messages = MutableSharedFlow<Message>(
        extraBufferCapacity = 16, onBufferOverflow = BufferOverflow.SUSPEND
    )
    override val messages: SharedFlow<Message> = _messages

    private val readingJob: Job
    private val writeMutex = Mutex()

    init {
        readingJob = startReading()
    }

    private fun startReading(): Job = scope.launch  {
        try {
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
        }
    }

    override suspend fun sendMessageTo(body: MessageBody, toId: String): Message {
        try {
            val message = Message(
                id = messageIdGenerator.generate(),
                fromId = myId,
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

            writeMutex.withLock {
                outputStream.writeInt(data.size)
                outputStream.write(data)
                outputStream.flush()
            }

            return message
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            throw FailedToSendMessageException()
        }
    }

    override suspend fun close() {
        onClose()
        scope.cancel()
        readingJob.cancel()
        try { socket.close() } catch (_: Exception) {}
    }
}