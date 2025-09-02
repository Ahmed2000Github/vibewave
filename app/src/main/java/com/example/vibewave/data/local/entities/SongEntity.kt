package com.example.vibewave.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val filePath: String,
    val thumbnail: String? = null,
    val drawableThumbnail: Int,
    val duration: Long,
    val isFavorite: Boolean = false,
    val lastPlayed: Long? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongEntity

        if (duration != other.duration) return false
        if (isFavorite != other.isFavorite) return false
        if (lastPlayed != other.lastPlayed) return false
        if (id != other.id) return false
        if (drawableThumbnail != other.drawableThumbnail) return false
        if (title != other.title) return false
        if (artist != other.artist) return false
        if (filePath != other.filePath) return false
        if (!thumbnail.contentEquals(other.thumbnail)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = duration.hashCode()
        result = 31 * result + isFavorite.hashCode()
        result = 31 * result + (lastPlayed?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + filePath.hashCode()
        result = 31 * result + (thumbnail?.hashCode() ?: 0)
        return result
    }
}