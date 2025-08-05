package com.example.vibewave.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.ui.screens.HomeScreen
import com.example.vibewave.presentation.ui.screens.MusicListScreen
import com.example.vibewave.presentation.ui.screens.PlayMusicScreen
import com.example.vibewave.presentation.ui.screens.WelcomeScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MusicList.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.MusicList.route) {
            MusicListScreen(navController)
        }
        composable(Screen.PlayMusic.route) { backStackEntry ->
//            val jsonSong = backStackEntry.arguments?.getString("song") ?: ""
//            val song = Gson().fromJson(Uri.decode(jsonSong), SongModel::class.java)
            val testSong = Song(
                id = "song_123",
                title = "Bohemian Rhapsody",
                artist = "Queen",
                duration = 354000, // 5:54 minutes
                uri = "content://media/external/audio/media/123",
                albumArtUri = "https://example.com/queen_album.jpg",
                isFavorite = true
            )
            PlayMusicScreen(navController,song = testSong)
        }
    }
}