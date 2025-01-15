package com.example.tictactoe_v0

fun findBestMoveMinimax(moves: List<Int>): Int {
    var bestScore = Int.MIN_VALUE
    var move = -1

    for (i in moves.indices) {
        if (moves[i] == -1) {
            val tempMoves = moves.toMutableList()
            tempMoves[i] = 1 // Computer's move
            val score = minimax(tempMoves, false)
            if (score > bestScore) {
                bestScore = score
                move = i
            }
        }
    }

    return move
}

fun minimax(moves: List<Int>, isMaximizing: Boolean): Int {
    val result = checkEndGame(moves)
    if (result != -1) {
        return when (result) {
            0 -> -10 // Player wins
            1 -> 10 // Computer wins
            2 -> 0 // Draw
            else -> 0
        }
    }

    if (isMaximizing) {
        var bestScore = Int.MIN_VALUE
        for (i in moves.indices) {
            if (moves[i] == -1) {
                val tempMoves = moves.toMutableList()
                tempMoves[i] = 1 // Computer's move
                val score = minimax(tempMoves, false)
                bestScore = maxOf(bestScore, score)
            }
        }
        return bestScore
    } else {
        var bestScore = Int.MAX_VALUE
        for (i in moves.indices) {
            if (moves[i] == -1) {
                val tempMoves = moves.toMutableList()
                tempMoves[i] = 0 // Player's move
                val score = minimax(tempMoves, true)
                bestScore = minOf(bestScore, score)
            }
        }
        return bestScore
    }
}

fun findRandomMove(moves: List<Int>): Int {
    val emptyIndices = moves.indices.filter { moves[it] == -1 }
    return emptyIndices.random()  // Selects a random index from empty spots
}

fun checkEndGame(m: List<Int>): Int {
    var win: Int = -1
    if (
        (m[0] == m[1] && m[1] == m[2] && m[2] == 0) ||
        (m[3] == m[4] && m[4] == m[5] && m[3] == 0) ||
        (m[6] == m[7] && m[7] == m[8] && m[6] == 0) ||
        (m[0] == m[3] && m[3] == m[6] && m[0] == 0) ||
        (m[1] == m[4] && m[4] == m[7] && m[1] == 0) ||
        (m[2] == m[5] && m[5] == m[8] && m[2] == 0) ||
        (m[0] == m[4] && m[4] == m[8] && m[0] == 0) ||
        (m[2] == m[4] && m[4] == m[6] && m[2] == 0)
    ) {
        win = 0  // Player 1 wins
    }
    if (
        (m[0] == m[1] && m[1] == m[2] && m[2] == 1) ||
        (m[3] == m[4] && m[4] == m[5] && m[3] == 1) ||
        (m[6] == m[7] && m[7] == m[8] && m[6] == 1) ||
        (m[0] == m[3] && m[3] == m[6] && m[0] == 1) ||
        (m[1] == m[4] && m[4] == m[7] && m[1] == 1) ||
        (m[2] == m[5] && m[5] == m[8] && m[2] == 1) ||
        (m[0] == m[4] && m[4] == m[8] && m[0] == 1) ||
        (m[2] == m[4] && m[4] == m[6] && m[2] == 1)
    ) {
        win = 1  // Player 2 wins
    }

    if(win == -1){
        var available = false
        for(i in 0..8){
            if(m[i] == -1){
                available = true
            }
        }
        if(!available){
            win = 2  // Draw
        }
    }
    return win
}