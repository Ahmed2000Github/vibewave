package com.example.vibewave.presentation.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.ui.screens.HomeScreen
import com.example.vibewave.presentation.ui.screens.MusicListScreen
import com.example.vibewave.presentation.ui.screens.PlayMusicScreen
import com.example.vibewave.presentation.ui.screens.WelcomeScreen
import com.example.vibewave.presentation.viewmodels.AudioPlayerViewModel
import com.example.vibewave.presentation.viewmodels.FavoriteSongsViewModel
import com.example.vibewave.presentation.viewmodels.GetAllSongsViewModel
import com.example.vibewave.presentation.viewmodels.RecentlyPlayedViewModel
import com.example.vibewave.presentation.viewmodels.SongViewModel
import com.google.gson.Gson


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    val audioPlayerViewModel: AudioPlayerViewModel = hiltViewModel()
    val allSongsViewModel: GetAllSongsViewModel = hiltViewModel()
    val favoriteSongsViewModel: FavoriteSongsViewModel = hiltViewModel()
    val recentlyPlayedViewModel: RecentlyPlayedViewModel = hiltViewModel()
    val  songViewModel: SongViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(
                navController, audioPlayerViewModel = audioPlayerViewModel,
                allSongsViewModel = allSongsViewModel,
                favoriteSongsViewModel = favoriteSongsViewModel,
                recentlyPlayedViewModel = recentlyPlayedViewModel,
            )
        }
        composable(Screen.MusicList.route) {
            MusicListScreen(
                navController, audioPlayerViewModel = audioPlayerViewModel,
                getAllSongsViewModel = allSongsViewModel
            )
        }
        composable(Screen.PlayMusic.route) { backStackEntry ->
            val jsonSong = backStackEntry.arguments?.getString("song") ?: ""
            if(jsonSong.isEmpty()) PlayMusicScreen(
                navController, initialSong = null, audioPlayerViewModel = audioPlayerViewModel,
                songViewModel = songViewModel,
                getAllSongsViewModel = allSongsViewModel
            )
            val song = Gson().fromJson(Uri.decode(jsonSong), Song::class.java)
            PlayMusicScreen(
                navController, initialSong = song, audioPlayerViewModel = audioPlayerViewModel,
                songViewModel = songViewModel,
                getAllSongsViewModel = allSongsViewModel
            )
        }
    }
}