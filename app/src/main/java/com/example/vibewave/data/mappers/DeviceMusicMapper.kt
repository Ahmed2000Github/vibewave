package com.example.vibewave.data.mappers

import com.example.vibewave.data.local.entities.MusicFileEntity
import com.example.vibewave.data.local.entities.SongEntity

class DeviceMusicMapper {
    fun mapToSong(deviceFile: MusicFileEntity): SongEntity {
        return SongEntity(
            id = deviceFile.id,
            title = deviceFile.title,
            artist = deviceFile.artist,
            duration = deviceFile.duration,
            filePath = deviceFile.filePath,
            thumbnail = deviceFile.thumbnail,
            isFavorite = deviceFile.isFavorite,
            lastPlayed = deviceFile.lastPlayed,
        )
    }
}