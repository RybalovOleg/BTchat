package io.salir.btchat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.salir.btchat.core.common.Result
import io.salir.btchat.domain.bluetooth.SessionManager
import io.salir.btchat.feature.chat.ChatScreen
import io.salir.btchat.welcome_screen.WelcomeScreen
import org.koin.compose.koinInject

@Composable
fun RootNavigation(
    sessionManager: SessionManager = koinInject()
) {
    val backStack = rememberNavBackStack(RootNavDests.Welcome)
    val sessionState by sessionManager.session.collectAsState()

    LaunchedEffect(sessionState) {
        val currentSession = (sessionState as? Result.Success)?.data
        if (currentSession != null) {
            if (backStack.lastOrNull() !is RootNavDests.Chats) {
                backStack.add(RootNavDests.Chats)
            }
        } else if (backStack.lastOrNull() is RootNavDests.Chats) {
            backStack.removeLastOrNull()
        }
    }

    NavDisplay(
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        entryProvider = entryProvider {
            entry<RootNavDests.Welcome> {
                WelcomeScreen(
                    navigateToHostChat = {
                        backStack.add(RootNavDests.HostNewChat)
                    },
                    navigateToJoinChat = {
                        backStack.add(RootNavDests.Chats)
                    }
                )
            }

            entry<RootNavDests.Chats> {

            }

            entry<RootNavDests.HostNewChat> {

            }

            entry<RootNavDests.Chat> { key ->
                ChatScreen()
            }

            entry<RootNavDests.Settings> {

            }
        }
    )
}
