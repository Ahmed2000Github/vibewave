package com.example.vibewave.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val duration: Long,
    val uri: String,
    val albumArtUri: String? = null,
    val genre: String? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val isFavorite: Boolean = false,
    val playCount: Int = 0,
    val lastPlayed: Long? = null,
    val bitrate: Int? = null,
    val timestamp: Long,
    val fileSize: Long? = null
)