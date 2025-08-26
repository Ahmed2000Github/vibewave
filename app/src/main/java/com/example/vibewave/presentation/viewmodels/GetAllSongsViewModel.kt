package com.example.vibewave.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.usecases.GetAllSongsUseCase
import com.example.vibewave.presentation.state.RecentMusicState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllSongsViewModel @Inject constructor(
    private val getAllSongsUseCase: GetAllSongsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<RecentMusicState>(RecentMusicState.Loading)
    val state: StateFlow<RecentMusicState> = _state

    init {
                    getAllSongs()
    }
     fun refreshSongs() {
        getAllSongs()
    }
    private fun getAllSongs() {
        viewModelScope.launch {
            delay(1000)
            getAllSongsUseCase()
                .catch { e -> _state.value = RecentMusicState.Error(e.message ?: "Unknown error") }
                .collect { songs ->
                    _state.value = RecentMusicState.Success(songs)
                }
        }
    }
}



