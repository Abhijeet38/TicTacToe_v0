package com.example.tictactoe_v0

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun MainScreen() {
    val currentScreen = remember { mutableStateOf("landing") }
    val selectedDifficulty = remember { mutableStateOf(Difficulty.NOOB) }

    when (currentScreen.value) {
        "landing" -> LandingPage(
            onStartGame = { difficulty ->
                selectedDifficulty.value = Difficulty.valueOf(difficulty)
                currentScreen.value = "game" }
        )
        "game" -> TTTScreen(
            onExitGame = { currentScreen.value = "landing" },
            difficulty = selectedDifficulty.value
        )
    }
}

