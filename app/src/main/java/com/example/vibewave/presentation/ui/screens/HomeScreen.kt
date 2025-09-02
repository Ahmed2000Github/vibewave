package com.example.vibewave.presentation.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.state.AllSongsState
import com.example.vibewave.presentation.state.FavoriteSongsState
import com.example.vibewave.presentation.state.RecentMusicState
import com.example.vibewave.presentation.ui.components.BottomNavBar
import com.example.vibewave.presentation.ui.components.CustomInput
import com.example.vibewave.presentation.ui.components.RecentPlayedSongCard
import com.example.vibewave.presentation.ui.components.RecentPlayedSongCardSkeleton
import com.example.vibewave.presentation.ui.components.SongCard
import com.example.vibewave.presentation.ui.components.SongCardSkeleton
import com.example.vibewave.presentation.viewmodels.AudioPlayerViewModel
import com.example.vibewave.presentation.viewmodels.FavoriteSongsViewModel
import com.example.vibewave.presentation.viewmodels.GetAllSongsViewModel
import com.example.vibewave.presentation.viewmodels.RecentlyPlayedViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    audioPlayerViewModel: AudioPlayerViewModel,
    allSongsViewModel: GetAllSongsViewModel,
    favoriteSongsViewModel: FavoriteSongsViewModel,
    recentlyPlayedViewModel: RecentlyPlayedViewModel
) {
    val text = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val favoriteSongsState by favoriteSongsViewModel.state.collectAsState()
    val recentlyPlayedState by recentlyPlayedViewModel.state.collectAsState()
    LaunchedEffect("") {
        snapshotFlow { allSongsViewModel.state.value }
            .collect { state ->
                if (state is AllSongsState.Success) audioPlayerViewModel.setSongs(state.songs)
            }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
            ) {
                Column {
                    CustomInput()
                    Spacer(modifier = Modifier.height(26.dp))
                    Row {
                        Text(
                            text = ("recently played").uppercase(),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        SeeAllMusic(navController = navController)
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                    when (val state = recentlyPlayedState) {
                        is RecentMusicState.Loading -> {
                            LazyRow(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(30.dp)
                            ) {
                                items(3) { index ->
                                    RecentPlayedSongCardSkeleton()
                                }
                            }
                        }

                        is RecentMusicState.Error -> {
                            Text(state.message ?: "Error Occurred")
                        }

                        is RecentMusicState.Success -> {
                            LazyRow(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(30.dp)
                            ) {
                                items(state.songs.size) { index ->
                                    RecentPlayedSongCard(
                                        navController = navController,
                                        song = state.songs[index]
                                    )
                                    if (index == state.songs.size - 1) Spacer(
                                        modifier = Modifier.width(
                                            10.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))
                    Row {
                        Text(
                            text = ("Favorite").uppercase(),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        SeeAllMusic(navController = navController)
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                    when (val state = favoriteSongsState) {
                        is FavoriteSongsState.Loading -> LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(30.dp)
                        ) {
                            items(10) { index ->
                                SongCardSkeleton()
                            }
                        }

                        is FavoriteSongsState.Success ->
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(30.dp)
                            ) {
                                items(state.songs.size) { index ->
                                    SongCard(
                                        navController = navController,
                                        initialSong = state.songs[index]
                                    )
                                }
                            }

                        is FavoriteSongsState.Error -> Text(
                            text = state.message ?: "Null Error",
                            color = Color.White
                        )
                    }

                }
            }
            if (audioPlayerViewModel.currentSong.value != null)
                Box {
                    BottomNavBar(navController, audioPlayerViewModel = audioPlayerViewModel)
                }
        }
    }
}


@Composable
fun SeeAllMusic(navController: NavController) {
    Text(
        modifier = Modifier.clickable {
            navController.navigate("musicList")
        },
        text = "See All",
        fontSize = 17.sp,
        color = Color.White.copy(alpha = .4f)
    )
}