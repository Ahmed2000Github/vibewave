package com.example.vibewave.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.usecases.ToggleSongFavoriteUseCase
import com.example.vibewave.presentation.state.RecentMusicState
import com.example.vibewave.presentation.state.SongCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongCardViewModel @Inject constructor(
    private val toggleSongFavoriteUseCase: ToggleSongFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<SongCardState>(SongCardState.Loading)
    val state: StateFlow<SongCardState> = _state
     fun toggleSongFavorite(songId:String) {
        viewModelScope.launch {

                delay(1000)
            toggleSongFavoriteUseCase(songId)
                    .catch { e ->
                        _state.value = SongCardState.Error(e.message ?: "Unknown error") }
                    .collect { song ->
                        _state.value = SongCardState.Success(song)
                    }
        }
    }
}