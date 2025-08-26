package com.example.vibewave.data.repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.vibewave.data.local.database.dao.SongDao
import com.example.vibewave.data.local.mediasource.DeviceMusicSource
import com.example.vibewave.data.mappers.DeviceMusicMapper
import com.example.vibewave.data.mappers.SongMapper
import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.repositories.SongsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class SongsRepositoryImpl(
    private val deviceMusicSource: DeviceMusicSource,
    private val songDao: SongDao,
    private val songMapper: SongMapper,
    private val deviceMusicMapper: DeviceMusicMapper
) : SongsRepository {

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun load() {
        val deviceMusicFound = deviceMusicSource.getAllDeviceSongs()
        val songFound =  deviceMusicFound.map { music -> deviceMusicMapper.mapToSong(music) }
        songFound.map { song -> songDao.insert(song) }
    }

    override fun getSongs(): Flow<List<Song>> {
        val deviceMusicFound = deviceMusicSource.getAllDeviceSongs()
        val songsFound = deviceMusicFound.map { music ->
            deviceMusicMapper.mapToSong(music)
        }
        val mappedSongs = songsFound.map { songMapper.mapFromEntity(it) }

        return flow { emit(mappedSongs) }
//        return dao.getSongs().map { list ->
//            list.map { songMapper.mapFromEntity(it) }
//        }
    }

    override fun getRecentlyPlayedSongs(): Flow<List<Song>> {
        return songDao.getSongs().map { list ->
            list.map { songMapper.mapFromEntity(it) }
        }
    }

    override fun getFavoriteSongs(): Flow<List<Song>> {
        return songDao.getSongs().map { list ->
            list.map { songMapper.mapFromEntity(it) }
        }
    }

    override  suspend fun toggleFavorite(songId: String) :Flow<Song>  {
         songDao.toggleSongFavorite(songId)
        return songDao.getSongById(songId).map { song -> songMapper.mapFromEntity(song) }
    }
}