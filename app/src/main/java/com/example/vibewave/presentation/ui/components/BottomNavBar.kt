package com.example.vibewave.presentation.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.navigation.Screen
import com.example.vibewave.presentation.viewmodels.AudioPlayerViewModel
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavBar(
    navController: NavController,
    audioPlayerViewModel: AudioPlayerViewModel
){
    val cardWidth = 60.dp
    val controlIconSize = 15.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                if (audioPlayerViewModel.currentSong.value != null) {
                    navController.navigate(Screen.PlayMusic.createRoute(null))
                }
            }
    ) {
        SongThumbnail(
            thumbnail = audioPlayerViewModel.currentSong.value!!.thumbnail,
            cardWidth = cardWidth,
            drawableId = audioPlayerViewModel.currentSong.value!!.drawableThumbnail
        )

        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .height(cardWidth)
                .padding(vertical = 5.dp)
                .weight(1f)
        ) {
            SimpleMarqueeText(
                modifier = Modifier.weight(1f),
                text = audioPlayerViewModel.currentSong.value!!.title ,
                fontSize = 18.sp,
                color = Color.White,
            )
            Text(
                text = audioPlayerViewModel.currentSong.value!!.artist.toString(),
                fontSize = 16.sp,
                color = Color.White.copy(alpha = .4f)
            )
        }
        Row (
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(0.dp))
            Image(
                painter = painterResource(id = R.drawable.play_prev),
                contentDescription = "play prev",
                modifier = Modifier.size(controlIconSize)
            )
            Box(
                modifier = Modifier .size(70.dp),
                contentAlignment = Alignment.Center
            ) {
                GradientPlayPauseButton(
                    isPlaying = audioPlayerViewModel.isPlaying.value ,
                    iconSize = 20.dp,
                    iconLeftPadding = 3.dp,
                    onToggle = {audioPlayerViewModel.toggle()},
                    modifier = Modifier.padding(10.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.play_next),
                contentDescription = "play next",
                modifier = Modifier.size(controlIconSize)
            )
        }

    }
}



@Composable
fun SimpleMarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
    color: Color = Color.White
) {
    var textWidth by remember { mutableStateOf(0) }
    var boxWidth by remember { mutableStateOf(0) }
    val offsetX = remember { Animatable(50f) }

    LaunchedEffect(textWidth,boxWidth) {
       val screenWidth = (textWidth + boxWidth).toFloat()
        while (true) {
            offsetX.animateTo(
                targetValue = -textWidth.toFloat(),
                animationSpec = tween(
                    durationMillis = 8000,
                    easing = LinearEasing
                )
            )
            offsetX.snapTo(screenWidth)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()
        .onGloballyPositioned { layoutCoordinates ->
            boxWidth = layoutCoordinates.size.width
        }
        .clipToBounds()
    ) {
        Text(
            modifier = modifier
                .offset(x = offsetX.value.dp)
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    textWidth = layoutCoordinates.size.width
                },
            text = text,
            fontSize = fontSize,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Visible
        )
    }
}