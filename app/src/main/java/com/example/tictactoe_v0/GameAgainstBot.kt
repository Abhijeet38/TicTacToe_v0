package com.example.tictactoe_v0

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun TTTScreen(onExitGame: () -> Unit, difficulty: Difficulty) {
    //true - player1's turn, false - computer's turn

    val playerTurn = remember { mutableStateOf(Random.nextBoolean()) }

    //true - player's move, false - computer's move, null - no move
    val moves = remember {
        mutableStateListOf<Boolean?>(null, null, null, null, null, null, null, null, null)
    }
    val win = remember { mutableStateOf<Win?>(null) }

    val onTap: (Offset, Int, Int) -> Unit = { offset, boardWidth, boardHeight ->
        if (playerTurn.value && win.value == null) {
            val x = (offset.x / (boardWidth / 3)).toInt()
            val y = (offset.y / (boardHeight / 3)).toInt()
            val index = y * 3 + x
            if (moves[index] == null) {
                moves[index] = true
                playerTurn.value = !playerTurn.value
                win.value = checkEndGame(moves)
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Tic Tac Toe", fontSize = 30.sp, modifier = Modifier.padding(top = 48.dp, bottom = 16.dp))
        Header(playerTurn.value)

        Board(moves, onTap)

        if(!playerTurn.value && win.value == null){
            CircularProgressIndicator(color = Color.Red, modifier = Modifier.padding(16.dp))

            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit){
                coroutineScope.launch {
                    delay(1500L)

                    // Minimax Algorithm : EXPERT Level & Random Move : NOOB Level
                    val computerMove = when(difficulty){
                        Difficulty.EXPERT -> findBestMoveMinimax(moves)
                        Difficulty.NOOB -> findRandomMove(moves)
                        else -> -1
                    }
                    moves[computerMove] = false
                    playerTurn.value = true
                    win.value = checkEndGame(moves)
                }
            }
        }

        if(win.value != null){
            when(win.value){
                Win.PLAYER -> {
                    Text(text = "Player Wins", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                Win.COMPUTER -> {
                    Text(text = "Computer Wins", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                Win.DRAW -> {
                    Text(text = "Draw", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                else -> {}
            }
            Button(onClick = {
                playerTurn.value = Random.nextBoolean()
                win.value = null
                for(i in 0..8){
                    moves[i] = null
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
fun Header(playerTurn: Boolean) {
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
                text = "Player", modifier = Modifier
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
                text = "Computer", modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
    }
}