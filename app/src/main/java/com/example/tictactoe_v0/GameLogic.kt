package com.example.tictactoe_v0

enum class Win{
    PLAYER,
    COMPUTER,
    DRAW
}

enum class Difficulty{
    NOOB,
    EXPERT
}

fun findBestMoveMinimax(moves: List<Boolean?>): Int {
    var bestScore = Int.MIN_VALUE
    var move = -1

    for (i in moves.indices) {
        if (moves[i] == null) {
            val tempMoves = moves.toMutableList()
            tempMoves[i] = false // Computer's move
            val score = minimax(tempMoves, false)
            if (score > bestScore) {
                bestScore = score
                move = i
            }
        }
    }

    return move
}

fun minimax(moves: List<Boolean?>, isMaximizing: Boolean): Int {
    val result = checkEndGame(moves)
    if (result != null) {
        return when (result) {
            Win.PLAYER -> -10 // Player wins
            Win.COMPUTER -> 10 // Computer wins
            Win.DRAW -> 0 // Draw
        }
    }

    if (isMaximizing) {
        var bestScore = Int.MIN_VALUE
        for (i in moves.indices) {
            if (moves[i] == null) {
                val tempMoves = moves.toMutableList()
                tempMoves[i] = false // Computer's move
                val score = minimax(tempMoves, false)
                bestScore = maxOf(bestScore, score)
            }
        }
        return bestScore
    } else {
        var bestScore = Int.MAX_VALUE
        for (i in moves.indices) {
            if (moves[i] == null) {
                val tempMoves = moves.toMutableList()
                tempMoves[i] = true // Player's move
                val score = minimax(tempMoves, true)
                bestScore = minOf(bestScore, score)
            }
        }
        return bestScore
    }
}

fun findRandomMove(moves: List<Boolean?>): Int {
    val emptyIndices = moves.indices.filter { moves[it] == null }
    return emptyIndices.random()  // Selects a random index from empty spots
}

fun checkEndGame(m: List<Boolean?>): Win? {
    var win: Win? = null
    if (
        (m[0] == m[1] && m[1] == m[2] && m[2] == true) ||
        (m[3] == m[4] && m[4] == m[5] && m[3] == true) ||
        (m[6] == m[7] && m[7] == m[8] && m[6] == true) ||
        (m[0] == m[3] && m[3] == m[6] && m[0] == true) ||
        (m[1] == m[4] && m[4] == m[7] && m[1] == true) ||
        (m[2] == m[5] && m[5] == m[8] && m[2] == true) ||
        (m[0] == m[4] && m[4] == m[8] && m[0] == true) ||
        (m[2] == m[4] && m[4] == m[6] && m[2] == true)
    ) {
        win = Win.PLAYER
    }
    if (
        (m[0] == m[1] && m[1] == m[2] && m[2] == false) ||
        (m[3] == m[4] && m[4] == m[5] && m[3] == false) ||
        (m[6] == m[7] && m[7] == m[8] && m[6] == false) ||
        (m[0] == m[3] && m[3] == m[6] && m[0] == false) ||
        (m[1] == m[4] && m[4] == m[7] && m[1] == false) ||
        (m[2] == m[5] && m[5] == m[8] && m[2] == false) ||
        (m[0] == m[4] && m[4] == m[8] && m[0] == false) ||
        (m[2] == m[4] && m[4] == m[6] && m[2] == false)
    ) {
        win = Win.COMPUTER
    }

    if(win == null){
        var available = false
        for(i in 0..8){
            if(m[i] == null){
                available = true
            }
        }
        if(!available){
            win = Win.DRAW
        }
    }
    return win
}