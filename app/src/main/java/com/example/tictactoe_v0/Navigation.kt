package com.example.tictactoe_v0

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun MainScreen() {
    val currentScreen = remember { mutableStateOf("landing") }

    when (currentScreen.value) {
        "landing" -> LandingPage(onStartGame = { currentScreen.value = "game" })
        "game" -> TTTScreen(onExitGame = { currentScreen.value = "landing" })
    }
}

