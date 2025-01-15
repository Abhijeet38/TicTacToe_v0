package com.example.tictactoe_v0

enum class Difficulty{
    NOOB,
    EXPERT,
    HUMAN,
    ONLINE
}

//enum class Win{
//    PLAYER,
//    COMPUTER,
//    DRAW
//}

data class GameState(
    val moves: List<Int> = List(9) { -1 },
    val playerTurn: Boolean = true,
    val win: Int = -1
)

data class GameStateOnline(
    val moves: List<Int> = List(9) { -1 },
    val win: Int = -1,
    val hostTurn: Boolean = true,
    val guestTurn: Boolean = false
)
