package com.example.vibewave.domain.models


    data class Song(
        val id: String,
        val title: String,
        val artist: String,
        val album: String? = null,
        val duration: Long,
        val timestamp: Long = System.currentTimeMillis(),
        val uri: String,
        val albumArtUri: String? = null,
        val genre: String? = null,
        val year: Int? = null,
        val trackNumber: Int? = null,
        val isFavorite: Boolean = false,
        val playCount: Int = 0,
        val lastPlayed: Long? = null,
        val bitrate: Int? = null,
        val fileSize: Long? = null
    ) {
        fun formattedDuration(): String {
            val seconds = (duration / 1000) % 60
            val minutes = (duration / (1000 * 60)) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

        // Helper function to check if recently played (within last 7 days)
        fun isRecentlyPlayed(): Boolean {
            return lastPlayed?.let {
                System.currentTimeMillis() - it < 7 * 24 * 60 * 60 * 1000
            } ?: false
        }
    }
