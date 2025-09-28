package com.example.vibewave.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.usecases.GetRecentlyPlayedSongsUseCase
import com.example.vibewave.domain.usecases.LoadDeviceSongsUseCase
import com.example.vibewave.presentation.state.RecentMusicState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentlyPlayedViewModel @Inject constructor(
    private val getRecentlyPlayedSongsUseCase: GetRecentlyPlayedSongsUseCase,
    private val loadDeviceSongsUseCase: LoadDeviceSongsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RecentMusicState>(RecentMusicState.Loading)
    val state: StateFlow<RecentMusicState> = _state

    init {
        getRecentlyPlayedSongs()
    }
    suspend fun refreshSongs(shouldShowLoading:Boolean = false) {
           if(shouldShowLoading) _state.value = RecentMusicState.Loading
            loadDeviceSongsUseCase()
            getRecentlyPlayedSongs()
    }
    private fun getRecentlyPlayedSongs() {
        viewModelScope.launch {
            getRecentlyPlayedSongsUseCase()
                .catch { e ->
                    _state.value = RecentMusicState.Error(e.message ?: "Unknown error") }
                .collect { songs ->
                    _state.value = RecentMusicState.Success(songs)
                }
        }
    }
}