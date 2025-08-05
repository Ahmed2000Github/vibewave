package com.example.vibewave.presentation.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.ui.components.CustomInput
import com.example.vibewave.presentation.ui.components.RecentPlayedSongCard
import com.example.vibewave.presentation.ui.components.SongCard


//navController.navigate("playMusic")

@Composable
fun HomeScreen(navController: NavController) {
    val text = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val testSong = Song(
        id = "song_123",
        title = "Bohemian Rhapsody",
        artist = "Queen",
        duration = 354000, // 5:54 minutes
        uri = "content://media/external/audio/media/123",
        albumArtUri = "https://example.com/queen_album.jpg",
        isFavorite = true
    )
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->

        Box(
            modifier = Modifier
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
                Row (   modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ){
                    RecentPlayedSongCard(navController= navController,song =  testSong)
                    RecentPlayedSongCard(navController= navController,song =  testSong)
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
                Column (
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    SongCard(navController= navController,song = testSong)
                    SongCard(navController= navController,song = testSong)
                    SongCard(navController= navController,song = testSong)
                    SongCard(navController= navController,song = testSong)
                    SongCard(navController= navController,song = testSong)

                }
            }
        }

    }
}


@Composable
fun SeeAllMusic(navController: NavController){
    Text(
        modifier = Modifier.clickable {
            navController.navigate("musicList")
        },
        text = "See All",
        fontSize = 17.sp,
        color = Color.White.copy(alpha = .4f)
    )
}