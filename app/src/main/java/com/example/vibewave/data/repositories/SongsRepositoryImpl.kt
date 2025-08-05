package com.example.vibewave.data.repositories

import com.example.vibewave.data.local.dao.SongDao
import com.example.vibewave.data.mappers.SongMapper
import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.repositories.SongsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SongsRepositoryImpl(
    private val dao: SongDao,
    private val mapper: SongMapper
) : SongsRepository {
    override suspend fun load() {
//        dao.insert(mapper.mapToEntity(song))
    }

    override fun getSongs(): Flow<List<Song>> {
        return dao.getSongs().map { list ->
            list.map { mapper.mapFromEntity(it) }
        }
    }

    override fun getRecentlyPlayedSongs(): Flow<List<Song>> {
        return dao.getSongs().map { list ->
            list.map { mapper.mapFromEntity(it) }
        }
    }

    override fun getFavoriteSongs(): Flow<List<Song>> {
        return dao.getSongs().map { list ->
            list.map { mapper.mapFromEntity(it) }
        }
    }

    override suspend fun addToFavorite(songId: String) {
//        dao.insert(mapper.mapToEntity(song))
    }

}