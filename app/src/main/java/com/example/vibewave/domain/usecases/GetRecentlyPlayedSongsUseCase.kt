package com.example.vibewave.domain.usecases

import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.repositories.SongsRepository
import kotlinx.coroutines.flow.Flow

class GetRecentlyPlayedSongsUseCase(
    private val repository: SongsRepository
) {
    operator fun invoke(): Flow<List<Song>> {
        return repository.getRecentlyPlayedSongs()
    }
}