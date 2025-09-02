package com.example.vibewave.presentation.state

import com.example.vibewave.domain.models.Song

sealed interface SearchSongState {
    data object Loading : SearchSongState
    data class Success(val songs: List<Song>) : SearchSongState
    data class Error(val message: String?) : SearchSongState
}