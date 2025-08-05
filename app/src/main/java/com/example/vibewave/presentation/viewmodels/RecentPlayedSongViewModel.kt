package com.example.vibewave.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.usecases.GetRecentlyPlayedSongsUseCase
import com.example.vibewave.presentation.state.RecentMusicState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentlyPlayedViewModel @Inject constructor(
    private val getRecentlyPlayedSongsUseCase: GetRecentlyPlayedSongsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RecentMusicState>(RecentMusicState.Loading)
    val state: StateFlow<RecentMusicState> = _state

    init {
        loadSongs()
    }

    private fun loadSongs() {
        viewModelScope.launch {
            getRecentlyPlayedSongsUseCase()
                .catch { e -> _state.value = RecentMusicState.Error(e.message ?: "Unknown error") }
                .collect { songs ->
                    _state.value = RecentMusicState.Success(songs)
                }
        }
    }
}