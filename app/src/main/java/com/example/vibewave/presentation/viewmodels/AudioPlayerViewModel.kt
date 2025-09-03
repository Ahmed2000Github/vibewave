package com.example.vibewave.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.usecases.GetRecentSongUseCase
import com.example.vibewave.presentation.state.SongCardState
import com.example.vibewave.services.AudioPlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import linc.com.amplituda.Amplituda
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val audioPlayerController: AudioPlayerController,
    private val amplituda: Amplituda,
    private val getRecentSongUseCase: GetRecentSongUseCase,
) : ViewModel() {
    val currentSong = mutableStateOf<Song?>(null)
    val songs = mutableStateOf<List<Song>?>(null)
    val isPlaying = mutableStateOf(false)
    val currentPosition = mutableLongStateOf(0L)
    val progress = mutableFloatStateOf(0f)
    val loopCount = mutableIntStateOf(0)
    val displayMode = mutableIntStateOf(0)
    val waveformData = mutableStateOf<List<Float>>(emptyList())
    val duration = mutableIntStateOf(1)
    val isPaused = mutableStateOf(false)

    init {
        audioPlayerController.apply {
            setProgressUpdateListener { currentPos, totalDuration ->
                duration.intValue = totalDuration
                currentPosition.longValue = currentPos.toLong()
                progress.floatValue = if (totalDuration > 0) {
                    currentPos.toFloat() / totalDuration.toFloat()
                } else 0f
            }
            setOnCompletion {
                when (displayMode.intValue) {
                    1 -> playRandom()
                    2 -> playNext()
                    else -> playNextWithoutLoop()
                }
                isPlaying.value = false
            }
        }
        viewModelScope.launch {
            getRecentSongUseCase()
                .catch { }
                .collect { song ->
                    setCurrentSong(song)
                }
        }
    }

    suspend fun config() {
        if (currentSong.value == null) return
        coroutineScope {
            val amplitudeJob = launch(Dispatchers.IO) {
                try {
                    val result = suspendCoroutine { continuation ->
                        amplituda.processAudio(currentSong.value!!.filePath)
                            .get({ result ->
                                continuation.resume(result)
                            }, { exception ->
                                continuation.resumeWithException(
                                    exception ?: Exception("Unknown error")
                                )
                            })
                    }

                    val amplitudesData: List<Int> = result.amplitudesAsList()
                    waveformData.value = amplitudesData.map { it.toFloat() }

                } catch (e: Exception) {
                    println("Failed to process amplitudes: ${e.message}")
                }
            }

            val playerJob = launch(Dispatchers.IO) {
                try {
                    startNewSong(currentSong.value!!.filePath)
                } catch (e: Exception) {
                    println("Failed to play: ${e.message}")
                }
            }
            joinAll(amplitudeJob, playerJob)
        }
    }

    fun setCurrentSong(newSong: Song) {
        currentSong.value = newSong
    }

    fun setSongs(newList: List<Song>) {
        songs.value = newList
    }

    private fun startNewSong(filePath: String) {
        audioPlayerController.playAudio(filePath)
        isPaused.value = false
        isPlaying.value = true
    }

    fun toggle() {
        if (currentSong.value == null) return
        if (isPlaying.value == true) {
            audioPlayerController.pause()
            isPlaying.value = false
            isPaused.value = true
        } else if (isPaused.value) {
            audioPlayerController.resume()
            isPaused.value = false
            isPlaying.value = true
        } else {
            startNewSong(currentSong.value!!.filePath)
        }

    }

    fun changeProgress(newProgress: Float) {
        progress.floatValue = newProgress
        audioPlayerController.seekTo((newProgress * duration.intValue).toInt())
    }

    fun changeDisplayMode() {
        if (displayMode.intValue == 2) displayMode.intValue = 0
        else displayMode.intValue += 1
    }

    fun changeLoopCount() {
        when (loopCount.intValue) {
            0 -> loopCount.intValue = 1
            1 -> loopCount.intValue = Int.MAX_VALUE
            Int.MAX_VALUE -> loopCount.intValue = 0
        }
        audioPlayerController.setLoopCount(loopCount.intValue)
    }

    fun playRandom() {
        if (songs.value == null) return
        val currentPosition = songs.value!!.indexOf(currentSong.value)
        val availablePositions = songs.value!!.indices.filter { it != currentPosition }

        val randomPosition = if (availablePositions.isNotEmpty()) {
            availablePositions.random()
        } else {
            currentPosition
        }
        currentSong.value = songs.value!![randomPosition]
        startNewSong(currentSong.value!!.filePath)
    }

    fun playNextWithoutLoop() {
        if (songs.value == null) return
        val currentPosition = songs.value!!.indexOf(currentSong.value)
        if (currentPosition + 1 >= songs.value!!.size) {
            isPlaying.value = false
            isPaused.value = false
            return
        }
        currentSong.value = songs.value!![currentPosition + 1]
        startNewSong(currentSong.value!!.filePath)
    }

    fun playNext() {

        if (songs.value == null) return
        val currentPosition = songs.value!!.indexOf(currentSong.value)
        val nextPosition = (currentPosition + 1) % songs.value!!.size
        currentSong.value = songs.value!![nextPosition]
        startNewSong(currentSong.value!!.filePath)
    }

    fun playPrev() {
        if (songs.value == null) return
        val currentPosition = songs.value!!.indexOf(currentSong.value)
        val prevPosition =
            if (currentPosition - 1 < 0) songs.value!!.size - 1 else currentPosition - 1
        currentSong.value = songs.value!![prevPosition]
        startNewSong(currentSong.value!!.filePath)
    }


}