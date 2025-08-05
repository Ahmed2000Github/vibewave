package com.example.vibewave.domain.repositories

import com.example.vibewave.domain.models.Song
import kotlinx.coroutines.flow.Flow


interface SongsRepository {
    suspend fun load()
    fun getSongs(): Flow<List<Song>>
    fun getRecentlyPlayedSongs(): Flow<List<Song>>
    fun getFavoriteSongs(): Flow<List<Song>>
    suspend fun addToFavorite(songId: String)
}