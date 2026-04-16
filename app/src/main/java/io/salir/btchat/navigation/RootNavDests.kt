package io.salir.btchat.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object RootNavDests {

    @Serializable
    object Welcome : NavKey

    @Serializable
    object Chats : NavKey

    @Serializable
    object HostNewChat : NavKey

    @Serializable
    object ConnectTo : NavKey

    @Serializable
    data class Chat(val chatId: String) : NavKey

    @Serializable
    object Settings : NavKey
}
