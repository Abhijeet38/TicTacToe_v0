package com.example.tictactoe_v0

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment


@Composable
fun MainScreen() {
    val currentScreen = remember { mutableStateOf("landing") }
    val selectedDifficulty = remember { mutableStateOf(Difficulty.NOOB) }
    val gameId = remember { mutableStateOf("") }
    val isHost = remember { mutableStateOf(false) }
    val showJoinDialog = remember { mutableStateOf(false) }

    fun onCreateRoom() {
        createRoom { id ->
            gameId.value = id
            isHost.value = true
            currentScreen.value = "gameOnline"
        }
    }

    fun onJoinRoom() {
        showJoinDialog.value = true
    }


    if (showJoinDialog.value) {
        JoinRoomDialog(
            onDismiss = { showJoinDialog.value = false },
            onJoin = { id ->
                gameId.value = id
                isHost.value = false
                currentScreen.value = "gameOnline"
                showJoinDialog.value = false
            }
        )
    }

    when (currentScreen.value) {
        "landing" -> LandingPage(
            onStartGame = { difficulty ->
                selectedDifficulty.value = Difficulty.valueOf(difficulty)
                if(selectedDifficulty.value!=Difficulty.ONLINE) {
                    currentScreen.value = if (selectedDifficulty.value == Difficulty.HUMAN) "gameHuman" else "gameBot"
                }

            },
            onCreateRoom = ::onCreateRoom,
            onJoinRoom = ::onJoinRoom
        )
        "gameBot" -> TTTScreen(
            onExitGame = { currentScreen.value = "landing" },
            difficulty = selectedDifficulty.value
        )
        "gameHuman" -> TTT2Screen(
            onExitGame = { currentScreen.value = "landing" }
        )
        "gameOnline" -> TTT3Screen(
            onExitGame = { currentScreen.value = "landing" },
            gameId = gameId.value,
            isHost = isHost.value
        )
    }
}

@Composable
fun JoinRoomDialog(onDismiss: () -> Unit, onJoin: (String) -> Unit) {
    var inputGameId by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = androidx.compose.ui.Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Enter Game ID", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

                TextField(
                    value = inputGameId,
                    onValueChange = { inputGameId = it },
                    label = { Text("Game ID") },
                    singleLine = true
                )

                Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }

                    Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))

                    Button(onClick = {
                        if (inputGameId.isNotBlank()) {
                            onJoin(inputGameId)
                        }
                    }) {
                        Text("Join")
                    }
                }
            }
        }
    }
}