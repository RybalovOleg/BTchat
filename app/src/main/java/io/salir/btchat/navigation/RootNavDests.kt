package io.salir.btchat.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object RootNavDests {

    @Serializable
    object Welcome : NavKey

    @Serializable
    object Chat : NavKey
}