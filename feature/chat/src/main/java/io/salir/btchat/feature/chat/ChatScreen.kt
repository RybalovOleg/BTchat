package io.salir.btchat.feature.chat

import androidx.compose.foundation.background
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
fun ChatScreen() {
    ChatScreenContent()
}

@Composable
private fun ChatScreenContent() {
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

        }
    }
}


@Composable
private fun ChatScreenPreview() {
    ChatScreenContent()
}

@Preview
@Composable
private fun ChatScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        ChatScreenPreview()
    }
}

@Preview
@Composable
private fun ChatScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        ChatScreenPreview()
    }
}