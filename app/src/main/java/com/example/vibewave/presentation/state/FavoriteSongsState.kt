package com.example.vibewave.presentation.state

import com.example.vibewave.domain.models.Song

sealed interface FavoriteSongsState {
    data object Loading : FavoriteSongsState
    data class Success(val songs: List<Song>) : FavoriteSongsState
    data class Error(val message: String?) : FavoriteSongsState
}