package com.example.tictactoe_v0

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlin.random.Random
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

@Composable
fun TTT3Screen(onExitGame: () -> Unit) {

    val gameId = "game123"
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("games/$gameId")

    val gameState = remember { mutableStateOf(GameState()) }
//    val gameState = GameState()
//    database.setValue(gameState)
//        .addOnSuccessListener {
//            Log.d("TTT3Screen", "Game state initialized")
//        }
//        .addOnFailureListener {
//            Log.e("TTT3Screen", "Failed to initialize game state: ${it.message}")
//        }

    // Firebase listener to sync game state
//    LaunchedEffect(Unit) {
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.getValue(GameState::class.java)?.let { newState ->
////                    Log.d("TTT3Screen", "New state: $newState")
//                    gameState.value = newState ?: GameState()
////                    Log.d("TTT3Screen", "Updated game state: ${gameState.value}")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("TTT3Screen", "Database error: ${error.message}")
//            }
//        })
//    }

    val onTap: (Offset, Int, Int) -> Unit = { offset, boardWidth, boardHeight ->
        if (gameState.value.win == null) {
            val cellWidth = boardWidth / 3
            val cellHeight = boardHeight / 3
            val x = (offset.x / cellWidth).toInt()
            val y = (offset.y / cellHeight).toInt()
            val index = y * 3 + x
            if (gameState.value.moves[index] == null) {
                val newMoves = gameState.value.moves.toMutableList()
                newMoves[index] = gameState.value.playerTurn
                val newGameState = gameState.value.copy(
                    moves = newMoves,
                    playerTurn = !gameState.value.playerTurn,
                    win = checkEndGame(newMoves)
                )
                gameState.value = newGameState
//                database.setValue(newGameState)
            }
        }

    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Tic Tac Toe", fontSize = 30.sp, modifier = Modifier.padding(top = 48.dp, bottom = 16.dp))
        Header2(gameState.value.playerTurn)

        Board(gameState.value.moves, onTap)


        if(gameState.value.win != null){
            when(gameState.value.win){
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
                val newMoves = List(9){null}
                val newGameState = GameState(moves = newMoves, playerTurn = Random.nextBoolean(), win = null)
                gameState.value = newGameState
//                database.setValue(newGameState)
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