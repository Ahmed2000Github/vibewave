package com.example.vibewave.domain.usecases

import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.repositories.SongsRepository
import kotlinx.coroutines.flow.Flow

class ToggleSongFavoriteUseCase(
    private val repository: SongsRepository
) {
    suspend operator fun invoke(songId:String) : Flow<Song>{
        return repository.toggleFavorite(songId)
    }
}

