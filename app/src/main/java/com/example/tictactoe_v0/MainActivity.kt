package com.example.tictactoe_v0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe_v0.ui.theme.TicTacToe_v0Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class Win{
    PLAYER,
    COMPUTER,
    DRAW
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToe_v0Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val currentScreen = remember { mutableStateOf("landing") }

    when (currentScreen.value) {
        "landing" -> LandingPage(onStartGame = { currentScreen.value = "game" })
        "game" -> TTTScreen(onExitGame = { currentScreen.value = "landing" })
    }
}

@Composable
fun LandingPage(onStartGame: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Tic Tac Toe!",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onStartGame) {
            Text(text = "Start Game", fontSize = 20.sp)
        }
    }
}


@Composable
fun TTTScreen(onExitGame: () -> Unit) {
    //true - player's turn, false - computer's turn
    val playerTurn = remember { mutableStateOf(true) }

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

                    // Minimax Algorithm : Pro Level : Undefeatable
                    val computerMove = findBestMoveMinimax(moves)
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
                playerTurn.value = true
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

@Composable
fun Board(moves: List<Boolean?>, onTap: (Offset) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(32.dp)
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onTap
                )
            }
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize(1f)) {
            Row(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth(1f)
                    .background(Color.Black)
            ) {}
            Row(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth(1f)
                    .background(Color.Black)
            ) {}
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize(1f)) {
            Column(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(1f)
                    .background(Color.Black)
            ) {}
            Column(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(1f)
                    .background(Color.Black)
            ) {}
        }
        Column(modifier = Modifier.fillMaxSize(1f)) {
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        Column(modifier = Modifier.weight(1f)) {
                            getComposableFromMove(move = moves[i * 3 + j])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getComposableFromMove(move: Boolean?) {
    when (move) {
        true -> Image(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(1f),
            colorFilter = ColorFilter.tint(Color.Blue)
        )

        false -> Image(
            painter = painterResource(id = R.drawable.ic_o),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(1f),
            colorFilter = ColorFilter.tint(Color.Red)
        )

        null -> Image(
            painter = painterResource(id = R.drawable.ic_null),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTTTScreen() {
    MainScreen()
}
