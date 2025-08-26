package com.example.vibewave.data.local.database.dao

import androidx.room.*
import com.example.vibewave.data.local.entities.SongEntity
import com.example.vibewave.domain.models.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: SongEntity)

    @Query("SELECT * FROM songs ORDER BY duration DESC")
    fun getSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSongById(songId: String): Flow<SongEntity>

    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun removeSong(songId: String)

    @Query("UPDATE songs SET isFavorite = NOT isFavorite WHERE id = :songId")
    suspend fun toggleSongFavorite(songId: String): Int
}