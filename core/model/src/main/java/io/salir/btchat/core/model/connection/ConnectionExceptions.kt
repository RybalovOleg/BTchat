package io.salir.btchat.core.model.connection

sealed class ConnectionException : Exception()

class FailedToConnectException : ConnectionException()

class HandshakeFailedException : ConnectionException()

class FailedToSendMessageException : ConnectionException()

class FailedToReceiveMessageException : ConnectionException()