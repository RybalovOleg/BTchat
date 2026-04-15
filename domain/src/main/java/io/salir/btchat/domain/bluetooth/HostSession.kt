package io.salir.btchat.domain.bluetooth

import io.salir.btchat.core.interfaces.bluetooth.Connection
import io.salir.btchat.core.model.bluetooth.Message
import io.salir.btchat.core.model.bluetooth.MessageBody
import io.salir.btchat.core.model.bluetooth.MessageStatusSentBody
import io.salir.btchat.core.model.bluetooth.Peer
import io.salir.btchat.core.model.bluetooth.RedirectedMessageBody
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class HostSession(
    private val conn: Connection.Host
) : Session() {

    override val scope = CoroutineScope(
        SupervisorJob(conn.scope.coroutineContext[Job]) + conn.scope.coroutineContext
    )

    override val myId: String get() = conn.myId

    private val _messages = MutableSharedFlow<Message>()
    override val messages: SharedFlow<Message> = _messages.asSharedFlow()

    init {
        conn.messages.onEach { message ->
            try {
                processMessage(message)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
            }
        }.catch { }.launchIn(scope)
    }

    override suspend fun close() {
        scope.cancel()
        conn.close()
    }

    override suspend fun sendMessage(body: MessageBody, toId: String) {
        conn.sendMessageTo(
            body = body, toId = toId
        )
    }

    private suspend fun processMessage(message: Message) {
        (message.body as? RedirectedMessageBody)?.let { body ->
            conn.sendMessageTo(
                body = MessageStatusSentBody(body.messageId),
                toId = message.fromId
            )
            redirectMessage(
                fromId = message.fromId,
                body = body
            )
        } ?: run { _messages.tryEmit(message) }
    }

    private suspend fun redirectMessage(
        fromId: String,
        body: RedirectedMessageBody
    ) {
        conn.sendMessageTo(
            body = body, toId = body.toId, fromId = fromId
        )
    }
}