package com.example.vibewave.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.navigation.Screen
import com.example.vibewave.presentation.state.RecentMusicState
import com.example.vibewave.presentation.state.SongCardState
import com.example.vibewave.presentation.viewmodels.SongCardViewModel

@Composable
fun SongCard(navController: NavController, initialSong: Song) {
    val cardWidth = 70.dp
    val viewModel: SongCardViewModel = hiltViewModel(
        key = "song_card_${initialSong.id}"
    )
    val state by viewModel.state.collectAsState()
    val song by remember(state) {
        derivedStateOf {
            when (val currentState = state) {
                is SongCardState.Success -> {
                    currentState.song
                }
                else -> initialSong
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.PlayMusic.createRoute(song))
            }
    ) {
        SongThumbnail(
            thumbnail = song.thumbnail,
            cardWidth = cardWidth,
            drawableId = R.drawable.album1
        )

        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .height(cardWidth)
                .padding(vertical = 5.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = song.title ,
                fontSize = 18.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artist.toString(),
                fontSize = 16.sp,
                color = Color.White.copy(alpha = .4f)
            )
        }
        Box(
            modifier = Modifier
                .height(cardWidth)
                .clickable {
                    viewModel.toggleSongFavorite(song.id)
                }
        ) {
            Image(
                painter = painterResource(id = if (song.isFavorite == true) R.drawable.heart_filled else R.drawable.heart),
                contentDescription = "favorite",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center)
            )
        }

    }

}
@Composable
fun SongCardSkeleton() {
    val cardWidth = 70.dp
    val infiniteTransition = rememberInfiniteTransition()

    // Animated alpha values for shimmer effect
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Thumbnail skeleton
        Box(
            modifier = Modifier
                .size(cardWidth)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = alpha))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .height(cardWidth)
                .padding(vertical = 5.dp)
                .weight(1f)
        ) {
            // Title skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray.copy(alpha = alpha))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Artist skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray.copy(alpha = alpha))
            )
        }

        // Favorite icon skeleton
        Box(
            modifier = Modifier
                .size(cardWidth)
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = alpha))
                    .align(Alignment.Center)
            )
        }
    }
}
