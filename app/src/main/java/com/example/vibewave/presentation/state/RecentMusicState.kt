package com.example.vibewave.presentation.state

import com.example.vibewave.domain.models.Song


sealed interface RecentMusicState {
    data object Loading : RecentMusicState
    data class Success(val songs: List<Song>) : RecentMusicState
    data class Error(val message: String?) : RecentMusicState
}

