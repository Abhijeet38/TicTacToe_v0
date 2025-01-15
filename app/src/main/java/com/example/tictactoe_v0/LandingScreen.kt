package com.example.tictactoe_v0

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LandingPage(onStartGame: (String) -> Unit, onCreateRoom: () -> Unit, onJoinRoom: () -> Unit) {

    val expanded = remember { mutableStateOf(false) }
    val selectedMode = remember { mutableStateOf<Difficulty?>(null) }
    val showMessage = remember { mutableStateOf(false) }
    val onlineModeSelected = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.game_logo),
            contentDescription = "Game Logo",
            modifier = Modifier
                .height(300.dp)
                .padding(40.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Welcome to Tic Tac Toe!",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for selecting game mode
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            TextButton(onClick = { expanded.value = !expanded.value }) {
                Text(text = "Select Game Mode: ${selectedMode.value ?: "None"}")
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text("NOOB") },
                    onClick = {
                        selectedMode.value = Difficulty.NOOB
                        expanded.value = false
                        onlineModeSelected.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("EXPERT") },
                    onClick = {
                        selectedMode.value = Difficulty.EXPERT
                        expanded.value = false
                        onlineModeSelected.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("HUMAN") },
                    onClick = {
                        selectedMode.value = Difficulty.HUMAN
                        expanded.value = false
                        onlineModeSelected.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("ONLINE") },
                    onClick = {
                        selectedMode.value = Difficulty.ONLINE
                        expanded.value = false
                        onlineModeSelected.value = true
                    }
                )
            }
        }

        // Show message if no game mode selected
        if (showMessage.value) {
            Text(
                text = "Please select a game mode!",
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (onlineModeSelected.value) {
            Button(onClick = onCreateRoom) {
                Text(text = "Create Room", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onJoinRoom) {
                Text(text = "Join Room", fontSize = 20.sp)
            }
        } else {
            Button(onClick = {
                if (selectedMode.value == null) {
                    showMessage.value = true
                } else {
                    val mode = selectedMode.value.toString()
                    showMessage.value = false
                    onStartGame(mode)
                }
            }) {
                Text(text = "Start Game", fontSize = 20.sp)
            }
        }

    }
}
