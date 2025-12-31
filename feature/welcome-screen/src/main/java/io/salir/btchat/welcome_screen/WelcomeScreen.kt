package io.salir.btchat.welcome_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    navigateToHostChat: () -> Unit,
    navigateToJoinChat: () -> Unit
) {
    WelcomeScreenContent(
        actions = remember {
            WelcomeScreenActions(
                onHostNewChat = navigateToHostChat,
                onJoinToChat = navigateToJoinChat
            )
        }
    )
}

@Composable
private fun WelcomeScreenContent(actions: WelcomeScreenActions) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.weight(2f)
                ) { }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = actions.onHostNewChat
                    ) { Text(stringResource(R.string.HostNewChatButton)) }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = actions.onJoinToChat
                    ) { Text(stringResource(R.string.JoinToChatButton)) }
                }
            }
        }
    }
}


@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreenContent(
        actions = WelcomeScreenActions(
            onHostNewChat = {},
            onJoinToChat = {}
        )
    )
}

@Preview
@Composable
private fun WelcomeScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        WelcomeScreenPreview()
    }
}

@Preview
@Composable
private fun WelcomeScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        WelcomeScreenPreview()
    }
}