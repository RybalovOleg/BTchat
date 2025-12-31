package io.salir.btchat.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.salir.btchat.feature.chat.ChatScreen
import io.salir.btchat.welcome_screen.WelcomeScreen

@Composable
fun RootNavigation() {
    val backStack = rememberNavBackStack(RootNavDests.Welcome)

    NavDisplay(
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        entryProvider = entryProvider {
            entry<RootNavDests.Welcome> {
                WelcomeScreen(
                    navigateToHostChat = {},
                    navigateToJoinChat = {}
                )
            }

            entry<RootNavDests.Chat> {
                ChatScreen()
            }
        },
    )
}