package com.example.tictactoe_v0

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun TTT2Screen(onExitGame: () -> Unit) {
    //true - player1's turn, false - player2's turn

    val playerTurn = remember { mutableStateOf(Random.nextBoolean()) }

    //true - player's move, false - computer's move, null - no move
    val moves = remember {
        mutableStateListOf<Int>(-1,-1,-1,-1,-1,-1,-1,-1,-1)
    }
    val win = remember { mutableStateOf<Int>(-1) }

    val onTap: (Offset, Int, Int) -> Unit = { offset, boardWidth, boardHeight ->
        if (playerTurn.value && win.value == -1) {
            val x = (offset.x / (boardWidth / 3)).toInt()
            val y = (offset.y / (boardHeight / 3)).toInt()
            val index = y * 3 + x
            if (moves[index] == -1) {
                moves[index] = 0
                playerTurn.value = false
                win.value = checkEndGame(moves)
            }
        }
        if (!playerTurn.value && win.value == -1) {
            val x = (offset.x / (boardWidth / 3)).toInt()
            val y = (offset.y / (boardHeight / 3)).toInt()
            val index = y * 3 + x
            if (moves[index] == -1) {
                moves[index] = 1
                playerTurn.value = true
                win.value = checkEndGame(moves)
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Tic Tac Toe", fontSize = 30.sp, modifier = Modifier.padding(top = 48.dp, bottom = 16.dp))
        Header2(playerTurn.value)

        Board(moves, onTap)


        if(win.value != -1){
            when(win.value){
                0 -> {
                    Text(text = "Player-1 Wins", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                1 -> {
                    Text(text = "Player-2 Wins", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                2 -> {
                    Text(text = "Draw", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                else -> {}
            }
            Button(onClick = {
                playerTurn.value = Random.nextBoolean()
                win.value = -1
                for(i in 0..8){
                    moves[i] = -1
                }
            }) {
                Text(text = "Play Again")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onExitGame) {
            Text(text = "Exit Game")
        }
    }

}

@Composable
fun Header2(playerTurn: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val playerBoxColor = if (playerTurn) Color.Blue else Color.LightGray
        val computerBoxColor = if (!playerTurn) Color.Red else Color.LightGray

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(playerBoxColor)
        ) {
            Text(
                text = "Player-1", modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(50.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(computerBoxColor)
        ) {
            Text(
                text = "Player-2", modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
    }
}
