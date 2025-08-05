package com.example.vibewave.presentation.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.navigation.Screen
import kotlin.math.absoluteValue
import kotlin.math.sin

@Composable
fun PlayMusicScreen(navController: NavController, song: Song) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var progress by remember { mutableFloatStateOf(0.3f) }
    var isPlaying by remember { mutableStateOf(false) }
    val cardWidth = (screenWidth - (32.dp))
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
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.popBackStack()
                            },
                        tint = Color.White
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = song.title,
                        fontSize = 18.sp,
                        color = Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
//                                navController.popBackStack()
                            },
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Image(
                    painter = painterResource(id = R.drawable.album1),
                    contentDescription = "App logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(cardWidth)
                        .shadow(
                            elevation = 10.dp, // Shadow intensity
                            shape = RoundedCornerShape(20.dp), // Match your clip shape
                            spotColor = Color.White.copy(alpha = 0.8f) // Optional shadow color
                        )
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.height(26.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.PlayMusic.createRoute(song!!))
                        }
                ) {

                    Column(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(vertical = 5.dp)
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = song.title,
                            fontSize = 18.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = song.artist,
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = .4f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                    ) {
                        Image(
                            painter = painterResource(id = if (song?.isFavorite == true) R.drawable.heart_filled else R.drawable.heart),
                            contentDescription = "favorite",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(26.dp))
                WaveformProgress(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    waveColor = Color(0xFF7B57E4),
                    inactiveWaveColor = Color(0xFFFFFFFF),
                    onProgressChange = { newProgress -> progress = newProgress },
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "1:04",
                        fontSize = 16.sp,
                        color = Color(0xFF7B57E4)
                    )
                    Text(
                        text = "3:29",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = .4f)
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Row (
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.random_display),
                        contentDescription = "play randoms",
                        modifier = Modifier.size(30.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.play_prev),
                        contentDescription = "play prev",
                        modifier = Modifier.size(30.dp)
                    )
                    Box(
                        modifier = Modifier .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        GradientPlayPauseButton(
                            isPlaying = isPlaying,
                            onToggle = { isPlaying = !isPlaying },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.play_next),
                        contentDescription = "play next",
                        modifier = Modifier.size(30.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.repeat_all),
                        contentDescription = "repeat",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }


}
@Composable
fun GradientPlayPauseButton(
    isPlaying: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF7E5EDA), Color(0xFFAC8FFD)),
        start = Offset(0f, 50f),
        end = Offset(50f, 0f)
    )

    Box(
        modifier = modifier
            .size(80.dp)
            .background(gradient, CircleShape)
            .padding(start = if (isPlaying)0.dp else 5.dp)
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
            contentDescription = if (isPlaying) "Pause" else "Play",
            modifier = Modifier.size(30.dp)
        )
    }
}
@Composable
fun WaveformProgress(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    waveColor: Color = Color.Blue,
    inactiveWaveColor: Color = waveColor.copy(alpha = 0.3f),
    waveCount: Int = 50,
    waveHeight: Dp = 50.dp,
    waveGap: Dp = 2.dp
) {
    val density = LocalDensity.current
    val waveGapPx = with(density) { waveGap.toPx() }

    Box(
        modifier = modifier
            .height(waveHeight)

            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val clickedProgress = (tapOffset.x / size.width).coerceIn(0f, 1f)
                    onProgressChange(clickedProgress)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val totalGapWidth = waveGapPx * (waveCount - 1)
            val availableWidth = size.width - totalGapWidth
            val waveWidth = availableWidth / waveCount

            val activeWaves = (waveCount * progress).coerceIn(0f, waveCount.toFloat())

            repeat(waveCount) { i ->
                val isActive = i < activeWaves
                val waveHeightPx = waveHeight.toPx() * (0.5f + 0.5f * sin(i * 0.5f).absoluteValue)
                val xPos = i * (waveWidth + waveGapPx)

                drawRoundRect(
                    color = if (isActive) waveColor else inactiveWaveColor,
                    topLeft = Offset(x = xPos, y = (size.height - waveHeightPx) / 2),
                    size = Size(width = waveWidth, height = waveHeightPx),
                    cornerRadius = CornerRadius(waveWidth * 0.4f)
                )
            }
        }
    }
}

