package com.example.tictactoe_v0

enum class Difficulty{
    NOOB,
    EXPERT,
    HUMAN
}

enum class Win{
    PLAYER,
    COMPUTER,
    DRAW
}

data class GameState(
    val moves: List<Boolean?> = List(9) { null },
    val playerTurn: Boolean = true,
    val win: Win? = null
)