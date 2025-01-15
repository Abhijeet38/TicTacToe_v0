package com.example.tictactoe_v0

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

fun createRoom(onRoomCreated: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val gameId = (Random.nextInt(10000, 99999)).toString() // Generate a unique game ID
    val initialGameState = GameStateOnline(
        moves = List(9) { -1 },
        win = -1,
        hostTurn = true,
        guestTurn = false
    )

    db.collection("games")
        .document(gameId)
        .set(initialGameState)
        .addOnSuccessListener {
            onRoomCreated(gameId) // Callback with the created game ID
        }
        .addOnFailureListener { e ->
            Log.w("Firebase", "Error creating room", e)
        }
}