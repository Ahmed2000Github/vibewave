package com.example.vibewave.data.repositories

import com.example.vibewave.data.local.database.dao.SongDao
import com.example.vibewave.data.local.entities.SongEntity
import com.example.vibewave.data.local.mediasource.DeviceMusicSource
import com.example.vibewave.data.mappers.DeviceMusicMapper
import com.example.vibewave.data.mappers.SongMapper
import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.repositories.SongsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SongsRepositoryImpl(
    private val deviceMusicSource: DeviceMusicSource,
    private val dao: SongDao,
    private val songMapper: SongMapper,
    private val deviceMusicMapper: DeviceMusicMapper
) : SongsRepository {
    override suspend fun load() {
        val deviceMusicFound = deviceMusicSource.getAllDeviceSongs()
        val songFound =  deviceMusicFound.map { music -> deviceMusicMapper.mapToSong(music) }
        songFound.map { song -> dao.insert(song) }
    }

    override fun getSongs(): Flow<List<Song>> {
//        val songs =(1..10).map {
//            SongEntity(
//                id = "song_$it",
//                title = "Bohemian Rhapsody $it",
//                artist = "Queen $it",
//                duration = 354000, // 5:54 minutes
//                uri = "content://media/external/audio/media/123",
//                albumArtUri = "https://example.com/queen_album.jpg",
//                isFavorite = false,
//                timestamp = System.currentTimeMillis(),
//            )
//        }
        return dao.getSongs().map { list ->
            list.map { songMapper.mapFromEntity(it) }
        }
    }

    override fun getRecentlyPlayedSongs(): Flow<List<Song>> {
        return dao.getSongs().map { list ->
            list.map { songMapper.mapFromEntity(it) }
        }
    }

    override fun getFavoriteSongs(): Flow<List<Song>> {
        return dao.getSongs().map { list ->
            list.map { songMapper.mapFromEntity(it) }
        }
    }

    override suspend fun addToFavorite(songId: String) {
//        dao.insert(songMapper.mapToEntity(song))
    }

}