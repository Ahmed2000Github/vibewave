package com.example.vibewave.presentation.state

import com.example.vibewave.domain.models.Song

sealed interface SongCardState {
    data object Loading : SongCardState
    data class Success(val song: Song) : SongCardState
    data class Error(val message: String?) : SongCardState
}