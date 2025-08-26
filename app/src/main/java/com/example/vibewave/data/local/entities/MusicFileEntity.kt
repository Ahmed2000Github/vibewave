package com.example.vibewave.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


data class MusicFileEntity(
    val id: String,
    val title: String,
    val artist: String,
    val filePath: String,
    val thumbnail: String? = null,
    val duration: Long,
    val isFavorite: Boolean = false,
    val lastPlayed: Long? = null,
)