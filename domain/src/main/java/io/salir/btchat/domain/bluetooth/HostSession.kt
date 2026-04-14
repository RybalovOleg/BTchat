package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.MessageBody
import io.salir.btchat.core.model.bluetooth.Peer
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock

internal class HostSession(
    private val conn: Connection.Host
) : Session() {

    override val me: Peer
        get() = conn.me

    private val _messages = MutableSharedFlow<Message>()
    override val messages: SharedFlow<Message> = _messages.asSharedFlow()

    init {
//        conn.messages.onEach { message ->
//            broadcastMessage(message)
//            _messages.emit(message)
//        }.launchIn(conn.scope)
    }

    override suspend fun close() {
        conn.close()
    }

    override suspend fun sendMessage(body: MessageBody, toId: String) {
//        val message = Message(
//            from = conn.thisDevice,
//            timestamp = Clock.System.now(),
//            body = body
//        )
//
//        broadcastMessage(message)
//        _messages.emit(message)

        TODO()
    }

//    private suspend fun broadcastMessage(message: Message): Unit = withContext(
//        conn.scope.coroutineContext[Job]!! + currentCoroutineContext()
//    ) {
//        conn.connectedDevices.value.forEach { device ->
//            launch {
//                conn.sendMessageTo(message, device)
//            }
//        }
//    }
}