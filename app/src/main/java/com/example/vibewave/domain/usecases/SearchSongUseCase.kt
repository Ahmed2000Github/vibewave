package com.example.vibewave.domain.usecases

import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.repositories.SongsRepository
import kotlinx.coroutines.flow.Flow

class SearchSongUseCase(
    private val repository: SongsRepository
) {
    operator fun invoke(query:String): Flow<List<Song>> {
        return repository.getFilteredSongs(query)
    }
}