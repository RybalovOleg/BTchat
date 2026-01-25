package io.salir.btchat.feature.host_new_chat_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HostNewChatScreen() {
    HostNewChatScreenContent()
}

@Composable
private fun HostNewChatScreenContent() {
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
            Column {

            }
        }
    }
}


@Composable
private fun HostNewChatScreenPreview() {
    HostNewChatScreenContent()
}

@Preview
@Composable
private fun HostNewChatScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        HostNewChatScreenPreview()
    }
}

@Preview
@Composable
private fun HostNewChatScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        HostNewChatScreenPreview()
    }
}