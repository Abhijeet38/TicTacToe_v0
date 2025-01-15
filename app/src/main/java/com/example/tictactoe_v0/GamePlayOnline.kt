package com.example.tictactoe_v0

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

@Composable
fun TTT3Screen(onExitGame: () -> Unit, gameId: String, isHost: Boolean) {

    val gameState = remember { mutableStateOf(GameStateOnline()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(gameId) {
        val gameDocRef = db.collection("games").document(gameId)

        gameDocRef.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Log.w("Firebase", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val gameData = documentSnapshot.data
                if (gameData != null) {
                    val updatedGameState = GameStateOnline(
                        moves = (gameData["moves"] as? List<Int>) ?: List(9) { -1 },
                        win = (if(gameData["win"] == -1) -1 else gameData["win"] as Long).toInt(),
                        hostTurn = gameData["hostTurn"] as? Boolean == true,
                        guestTurn = gameData["guestTurn"] as? Boolean == true
                    )
                    gameState.value = updatedGameState
                }
            }
        }
    }
    // Set the game state to Firestore whenever the game state changes
    LaunchedEffect(gameState.value) {
        val gameStateData = hashMapOf(
            "moves" to gameState.value.moves,
            "win" to gameState.value.win,
            "hostTurn" to gameState.value.hostTurn,
            "guestTurn" to gameState.value.guestTurn
        )

        // Set game state in Firestore (replace with your Firestore collection and document)
        db.collection("games") // Collection name is "games"
            .document(gameId) // Use a unique game ID to identify the document
            .set(gameStateData) // Set the game state
            .addOnSuccessListener {
                Log.d("Firebase", "Game state successfully updated")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error updating game state", e)
            }
    }

    val onTap: (Offset, Int, Int) -> Unit = { offset, boardWidth, boardHeight ->
        if (gameState.value.win == -1) {
            val cellWidth = boardWidth / 3
            val cellHeight = boardHeight / 3
            val x = (offset.x / cellWidth).toInt()
            val y = (offset.y / cellHeight).toInt()
            val index = y * 3 + x
            if ((gameState.value.moves[index] == -1) && (gameState.value.hostTurn == isHost)) {
                val newMoves = gameState.value.moves.toMutableList()
                newMoves[index] = if (isHost) 0 else 1
                val newGameState = gameState.value.copy(
                    moves = newMoves,
                    hostTurn = !isHost,
                    guestTurn = isHost,
                    win = checkEndGame(newMoves)
                )
                gameState.value = newGameState
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Tic Tac Toe", fontSize = 30.sp, modifier = Modifier.padding(top = 48.dp, bottom = 16.dp))
        Text(text = "Game ID: $gameId", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))
        HeaderOnline(isHostTurn = gameState.value.hostTurn, isHost = isHost)

        Board(
            moves = gameState.value.moves,
            onTap = onTap
        )

        if(gameState.value.win != -1){
            when (gameState.value.win) {
                0 -> {
                    Text(text = if (isHost) "You Win!" else "Opponent Wins!", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                1 -> {
                    Text(text = if (!isHost) "You Win!" else "Opponent Wins!", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                2 -> {
                    Text(text = "It's a Draw!", fontSize = 25.sp, modifier = Modifier.padding(16.dp))
                }
                else -> {}
            }
            Button(onClick = {
                val newMoves = List(9){-1}
                val randomTurn = Random.nextBoolean()
                val newGameState = GameStateOnline(moves = newMoves, hostTurn = randomTurn, guestTurn = !randomTurn, win = -1)
                gameState.value = newGameState
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
fun HeaderOnline(isHostTurn: Boolean, isHost: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(16.dp)
    ) {
        val hostBoxColor = if (isHostTurn) Color.Blue else Color.LightGray
        val guestBoxColor = if (!isHostTurn) Color.Red else Color.LightGray

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(hostBoxColor)
        ) {
            Text(
                text = if (isHost) "Your Turn" else "Opponent's Turn",
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(50.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(guestBoxColor)
        ) {
            Text(
                text = if (!isHost) "Your Turn" else "Opponent's Turn",
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center),
                color = Color.White
            )
        }
    }
}
