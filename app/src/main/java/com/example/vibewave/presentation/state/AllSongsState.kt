package com.example.vibewave.presentation.state

import com.example.vibewave.domain.models.Song

sealed interface AllSongsState {
    data object Loading : AllSongsState
    data class Success(val songs: List<Song>) : AllSongsState
    data class Error(val message: String?) : AllSongsState
}
