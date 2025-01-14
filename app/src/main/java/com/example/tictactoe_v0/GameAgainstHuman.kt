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
        mutableStateListOf<Boolean?>(null, null, null, null, null, null, null, null, null)
    }
    val win = remember { mutableStateOf<Win?>(null) }

    val onTap : (Offset) -> Unit = {
        if(playerTurn.value && win.value == null){
            val x = (it.x / 333).toInt()   //Dividing by 333 as offset returns value b/w 0 to 1000
            val y = (it.y / 333).toInt()
            val index = y * 3 + x
            if(moves[index] == null) {
                moves[index] = true
                playerTurn.value = false
                win.value = checkEndGame(moves)
            }
        }
        if(!playerTurn.value && win.value == null){
            val x = (it.x / 333).toInt()
            val y = (it.y / 333).toInt()
            val index = y * 3 + x
            if(moves[index] == null) {
                moves[index] = false
                playerTurn.value = true
                win.value = checkEndGame(moves)
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Tic Tac Toe", fontSize = 30.sp, modifier = Modifier.padding(top = 48.dp, bottom = 16.dp))
        Header2(playerTurn.value)

        Board(moves, onTap)


        if(win.value != null){
            when(win.value){
                Win.PLAYER -> {
                    Text(text = "Player-1 Wins", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                Win.COMPUTER -> {
                    Text(text = "Player-2 Wins", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
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
