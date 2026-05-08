package io.salir.btchat.welcome_screen

internal sealed class Intent

internal class HostNewSessionIntent : Intent()

internal class JoinToSessionIntent : Intent()