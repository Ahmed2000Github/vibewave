package com.example.vibewave.data.local.dao

import androidx.room.*
import com.example.vibewave.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: SongEntity)

    @Query("SELECT * FROM songs ORDER BY timestamp DESC LIMIT 20")
    fun getSongs(): Flow<List<SongEntity>>

    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun removeSong(songId: String)
}