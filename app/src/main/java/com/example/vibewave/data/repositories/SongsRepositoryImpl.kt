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


@RequiresApi(Build.VERSION_CODES.O)
class SongsRepositoryImpl(
    private val deviceMusicSource: DeviceMusicSource,
    private val songDao: SongDao,
    private val songMapper: SongMapper,
    private val deviceMusicMapper: DeviceMusicMapper
) : SongsRepository {

    override suspend fun load() {
        val deviceMusicFound = deviceMusicSource.getAllDeviceSongs()
        val songFound =  deviceMusicFound.map { music -> deviceMusicMapper.mapToSong(music) }
        songFound.map { song -> songDao.upsertWithSpecificUpdates(song) }
    }

    override fun getSongs(): Flow<List<Song>> {
        return songDao.getSongs().map { list ->
            list.map {
                println(it)
                songMapper.mapFromEntity(it) }
        }
    }

    override fun searchSongs(query:String): Flow<List<Song>> {
        return songDao.searchSongs(query).map { list ->
            list.map {
                songMapper.mapFromEntity(it) }
        }
    }

    override fun getRecentlyPlayedSongs(): Flow<List<Song>> {
        return songDao.getRecentPlayedSongs().map { list ->
            list.map { songMapper.mapFromEntity(it) }
        }
    }

    override fun getFavoriteSongs(): Flow<List<Song>> {
        return songDao.getFavoriteSongs().map { list ->
            list.map { songMapper.mapFromEntity(it) }
        }
    }

    override  suspend fun toggleFavorite(songId: String) :Flow<Song>  {
         songDao.toggleSongFavorite(songId)
        return songDao.getSongById(songId).map { song -> songMapper.mapFromEntity(song) }
    }


    override  suspend fun updateLastPlay(songId: String) :Flow<Song>  {
         songDao.updateLastPlay(songId, System.currentTimeMillis())
        return songDao.getSongById(songId).map { song -> songMapper.mapFromEntity(song) }
    }
    override suspend fun getRecentSong() :Flow<Song>  {
        return songDao.getRecentSong().map { song -> songMapper.mapFromEntity(song) }
    }
}