package com.example.vibewave.data.local.database.dao

import androidx.room.*
import com.example.vibewave.data.local.entities.SongEntity
import com.example.vibewave.domain.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: SongEntity): Long

    @Transaction
    suspend fun upsertWithSpecificUpdates(song: SongEntity) {
        val existingSong = getSongById(song.id).firstOrNull()
        if (existingSong == null) {
            insert(song)
        } else {
            updateColumns(song.id, song.thumbnail)
        }
    }

    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getTotalSongCount(): Int

    @Query("UPDATE songs SET thumbnail = :thumbnail WHERE id = :id")
    suspend fun updateColumns(id: String, thumbnail: String?)

    @Query("SELECT * FROM songs ORDER BY duration DESC")
    fun getSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs ORDER BY lastPlayed DESC Limit 5")
    fun getRecentPlayedSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs Where isFavorite == 1 ORDER BY duration DESC")
    fun getFavoriteSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSongById(songId: String): Flow<SongEntity>

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :title || '%' ORDER BY duration DESC")
    fun searchSongs(title: String): Flow<List<SongEntity>>


    @Query("SELECT * FROM songs ORDER BY lastPlayed DESC Limit 1")
    fun getRecentSong(): Flow<SongEntity>

    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun removeSong(songId: String)

    @Query("UPDATE songs SET isFavorite = NOT isFavorite WHERE id = :songId")
    suspend fun toggleSongFavorite(songId: String): Int

    @Query("UPDATE songs SET lastPlayed = :lastPlay WHERE id = :songId")
    suspend fun updateLastPlay(songId: String, lastPlay: Long): Int
}