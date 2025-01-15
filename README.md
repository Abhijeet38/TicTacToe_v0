# Tic Tac Toe

A Tic Tac Toe game built with Kotlin, Jetpack Compose, and Firebase Firestore for online multiplayer functionality.

## Features

- Single-player mode with two difficulty levels: Noob Bot and Expert Bot.
- Two-player mode on the same device.
- Online multiplayer mode with the ability to create and join rooms.
- Real-time game state synchronization using Firebase Firestore.


## Code Structure

- `MainActivity.kt`: The main entry point of the app.
- `MainScreen.kt`: Contains the main navigation logic and composables for different screens.
- `LandingPage.kt`: The landing page where users select the game mode.
- `TTTScreen.kt`, `TTT2Screen.kt`, `TTT3Screen.kt`: Composables for different game modes.
- `GameData.kt`: Data classes for game state management.
- `CreateRoom.kt`: Function to create a new game room in Firestore.
- `JoinRoomDialog.kt`: Composable for joining an existing game room.
