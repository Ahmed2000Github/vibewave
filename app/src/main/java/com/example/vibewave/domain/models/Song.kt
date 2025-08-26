package com.example.vibewave.domain.models

import androidx.room.ColumnInfo


data class Song(
        val id: String,
        val title: String,
        val artist: String,
        val filePath: String,
        val thumbnail: String? = null,
        val duration: Long,
        val isFavorite: Boolean = false,
        val lastPlayed: Long? = null,
    ) {
        fun formattedDuration(): String {
            val seconds = (duration / 1000) % 60
            val minutes = (duration / (1000 * 60)) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

        fun isRecentlyPlayed(): Boolean {
            return lastPlayed?.let {
                System.currentTimeMillis() - it < 7 * 24 * 60 * 60 * 1000
            } ?: false
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        if (duration != other.duration) return false
        if (isFavorite != other.isFavorite) return false
        if (lastPlayed != other.lastPlayed) return false
        if (id != other.id) return false
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
