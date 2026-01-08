package io.salir.btchat.welcome_screen

import androidx.compose.runtime.Stable

@Stable
internal data class WelcomeScreenActions(
    val onHostNewChat: () -> Unit,
    val onJoinToChat: () -> Unit,
)