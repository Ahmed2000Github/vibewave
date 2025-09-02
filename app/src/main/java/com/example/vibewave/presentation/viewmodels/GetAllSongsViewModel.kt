package com.example.vibewave.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.usecases.GetAllSongsUseCase
import com.example.vibewave.presentation.state.AllSongsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("CAST_NEVER_SUCCEEDS")
@HiltViewModel
class GetAllSongsViewModel @Inject constructor(
    private val getAllSongsUseCase: GetAllSongsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AllSongsState>(AllSongsState.Loading)
    val state: StateFlow<AllSongsState> = _state

    init {
                    getAllSongs()
    }
     fun refreshSongs() {
        getAllSongs()
    }

        fun updateSong(updatedSong: Song) {
            val innerState = _state.value
            if (innerState is AllSongsState.Success) {
               val updatedSongs =  innerState.songs.map { song ->
                   if (song.filePath == updatedSong.filePath) {
                       updatedSong
                   } else {
                       song
                   }
               }
               _state.value = AllSongsState.Success(updatedSongs)
           }

        }
    private fun getAllSongs() {
        viewModelScope.launch {
            getAllSongsUseCase()
                .catch { e -> _state.value = AllSongsState.Error(e.message ?: "Unknown error") }
                .collect { songs ->
                    _state.value = AllSongsState.Success(songs)
                }
        }
    }

}



