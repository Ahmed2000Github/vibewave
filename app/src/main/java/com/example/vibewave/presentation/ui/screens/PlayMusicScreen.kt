package com.example.vibewave.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.state.SongCardState
import com.example.vibewave.presentation.ui.components.GradientPlayPauseButton
import com.example.vibewave.presentation.ui.components.SongThumbnail
import com.example.vibewave.presentation.viewmodels.AudioPlayerViewModel
import com.example.vibewave.presentation.viewmodels.GetAllSongsViewModel
import com.example.vibewave.presentation.viewmodels.SongViewModel
import com.example.vibewave.utils.FormatUtils.formatTime

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlayMusicScreen(
    navController: NavController,
    initialSong: Song?,
    audioPlayerViewModel: AudioPlayerViewModel,
    songViewModel: SongViewModel,
    getAllSongsViewModel: GetAllSongsViewModel
) {

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val cardWidth = (screenWidth - (32.dp))
    val controlIconSize = 25.dp
    fun getDisplayIcon(): Int {
        return when (audioPlayerViewModel.displayMode.value) {
            1 -> R.drawable.single_display
            0 -> R.drawable.random_display
            else -> {
                R.drawable.loop_display
            }
        }
    }

    fun getRepeatIcon(): Int {
        return when (audioPlayerViewModel.loopCount.value) {
            1 -> R.drawable.repeat_this
            0 -> R.drawable.repeat_all
            else -> {
                R.drawable.repeat_infinity
            }
        }
    }

    val songCardState by songViewModel.state.collectAsState()
    val song = remember(songCardState) {
        initialSong ?: audioPlayerViewModel.currentSong.value!!
    }
    LaunchedEffect("") {
        if (initialSong != null) {
            audioPlayerViewModel.setCurrentSong(song)
            getAllSongsViewModel.updateSong(song)
            songViewModel.updateLastPlay(song.id)
            audioPlayerViewModel.config(true)
        } else
            audioPlayerViewModel.config()

    }

    DisposableEffect(Unit) {
        onDispose {
//            audioPlayer?.cleanup()
        }
    }

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
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = song.title,
                        fontSize = 18.sp,
                        color = Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(20.dp))
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
                SongThumbnail(
                    thumbnail = song.thumbnail,
                    cardWidth = cardWidth,
                    drawableId = song.drawableThumbnail
                )

                Spacer(modifier = Modifier.height(26.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                    Spacer(modifier = Modifier.width(26.dp))
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .clickable {
                                songViewModel.toggleSongFavorite(song.id)
                            }
                    ) {
                        when(val _state = songCardState){
                            is SongCardState.Success -> Image(
                                painter = painterResource(id = if (_state.song.isFavorite == true) R.drawable.heart_filled else R.drawable.heart),
                                contentDescription = "favorite",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                            )
                            is SongCardState.Error -> {
                                Image(
                                    painter = painterResource(id = if (song.isFavorite == true) R.drawable.heart_filled else R.drawable.heart),
                                    contentDescription = "favorite",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.Center)
                                )
                            }
                            is SongCardState.Loading -> {Image(
                                painter = painterResource(id = if (song.isFavorite == true) R.drawable.heart_filled else R.drawable.heart),
                                contentDescription = "favorite",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                            )

                            }
                        }

                    }

                }
                Spacer(modifier = Modifier.height(26.dp))
                if (audioPlayerViewModel.waveformData.value.isEmpty()) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                    )
                } else {
                    WaveformProgress(
                        progress = audioPlayerViewModel.progress.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        waveColor = Color(0xFF7B57E4),
                        inactiveWaveColor = Color(0xFFFFFFFF),
                        amplitudes = audioPlayerViewModel.waveformData.value,
                        onProgressChange = { progress ->
                            audioPlayerViewModel.changeProgress(
                                progress
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Current position (left-aligned)
                    Text(
                        modifier = Modifier.weight(1f),
                        text = formatTime(audioPlayerViewModel.currentPosition.value),
                        fontSize = 16.sp,
                        color = Color(0xFF7B57E4)
                    )

                    Text(
                        text = formatTime(audioPlayerViewModel.duration.value.toLong()),
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = getDisplayIcon()),
                        contentDescription = "play randoms",
                        modifier = Modifier
                            .size(controlIconSize)
                            .clickable { audioPlayerViewModel.changeDisplayMode() }
                    )
                    Spacer(modifier = Modifier.width(0.dp))
                    Image(
                        painter = painterResource(id = R.drawable.play_prev),
                        contentDescription = "play prev",
                        modifier = Modifier
                            .size(controlIconSize)
                            .clickable {
                                audioPlayerViewModel.playPrev()
                            }
                    )
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GradientPlayPauseButton(
                            isPlaying = audioPlayerViewModel.isPlaying.value,
                            onToggle = { audioPlayerViewModel.toggle() },
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.play_next),
                        contentDescription = "play next",
                        modifier = Modifier
                            .size(controlIconSize)
                            .clickable {
                                audioPlayerViewModel.playNext()
                            }
                    )
                    Spacer(modifier = Modifier.width(0.dp))
                    Image(
                        painter = painterResource(id = getRepeatIcon()),
                        contentDescription = "repeat",
                        modifier = Modifier
                            .size(controlIconSize)
                            .clickable { audioPlayerViewModel.changeLoopCount() }
                    )
                }
            }
        }
    }


}

@Preview
@Composable
fun WaveformProgress(
    progress: Float = .7f,
    amplitudes: List<Float> = List(size = 7) { 2F },
    onProgressChange: (Float) -> Unit = {},
    modifier: Modifier = Modifier,
    waveColor: Color = Color(0xFF7B57E4),
    inactiveWaveColor: Color = waveColor.copy(alpha = 0.3f),
    waveCount: Int = 70,
    waveHeight: Dp = 70.dp,
    waveGap: Dp = 2.dp
) {
    val density = LocalDensity.current
    val waveGapPx = with(density) { waveGap.toPx() }
    val waveHeightPx = with(density) { waveHeight.toPx() }

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
            if (amplitudes.isEmpty()) return@Canvas

            // Calculate how many amplitude samples to use per visual wave
            val samplesPerWave = (amplitudes.size / waveCount).coerceAtLeast(1)
            val maxAmplitude = amplitudes.maxOrNull() ?: 1f
            val minAmplitude = amplitudes.minOrNull() ?: 0f
            val diff = maxAmplitude - minAmplitude
            val normalizedAmplitudes = if (diff > 0) {
                amplitudes.map { (it - minAmplitude) / diff }
            } else {
                amplitudes
            }

            val totalGapWidth = waveGapPx * (waveCount - 1)
            val availableWidth = size.width - totalGapWidth
            val waveWidth = availableWidth / waveCount

            val activeWaves = (waveCount * progress).coerceIn(0f, waveCount.toFloat())

            repeat(waveCount) { i ->
                // Calculate average amplitude for this wave segment
                val startSample = i * samplesPerWave
                val endSample = (i + 1) * samplesPerWave
                val segmentAmplitudes = normalizedAmplitudes.subList(
                    startSample.coerceAtMost(normalizedAmplitudes.lastIndex),
                    endSample.coerceAtMost(normalizedAmplitudes.size)
                )

                val averageAmplitude = if (segmentAmplitudes.isNotEmpty()) {
                    segmentAmplitudes.average().toFloat()
                } else {
                    0.5f
                }
                val isActive = i < activeWaves
                val currentWaveHeight = waveHeightPx * averageAmplitude.coerceIn(0.1f, 1f)
                val xPos = i * (waveWidth + waveGapPx)

                drawRoundRect(
                    color = if (isActive) waveColor else inactiveWaveColor,
                    topLeft = Offset(x = xPos, y = (size.height - currentWaveHeight) / 2),
                    size = Size(width = waveWidth, height = currentWaveHeight),
                    cornerRadius = CornerRadius(waveWidth * 0.4f)
                )
            }
        }
    }
}

