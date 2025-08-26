package com.example.vibewave.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.state.RecentMusicState
import com.example.vibewave.presentation.ui.components.SongCard
import com.example.vibewave.presentation.viewmodels.RecentlyPlayedViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vibewave.presentation.ui.components.SongCardSkeleton
import kotlinx.coroutines.launch


@Composable
fun MusicListScreen(navController: NavController,viewModel: RecentlyPlayedViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()


    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Column {
                Row {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "Back button clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            },
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.refreshSongs()
                                }
                            },
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                when (val currentState = state) {
                    is RecentMusicState.Loading -> LazyColumn (  modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(30.dp)) {
                        items(10) { index ->
                            SongCardSkeleton()
                        }
                    }
                    is RecentMusicState.Success ->
                        LazyColumn (  modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(30.dp)) {
                            items(currentState.songs.size) { index ->
                                SongCard(
                                    navController = navController,
                                    initialSong = currentState.songs[index]
                                )
                            }
                    }
                    is RecentMusicState.Error -> Text(text= currentState.message ?:"Null Error",color=Color.White)
                }
                Spacer(modifier = Modifier.height(26.dp))
            }
        }

    }
}