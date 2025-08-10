package com.example.vibewave.data.mappers

import com.example.vibewave.data.local.entities.SongEntity
import com.example.vibewave.domain.models.Song

class SongMapper {
    fun mapFromEntity(entity: SongEntity): Song {
        return Song(
            id = entity.id,
            title = entity.title,
            artist = entity.artist,
            duration = entity.duration,
            filePath = entity.filePath,
            thumbnail = entity.thumbnail,
            isFavorite = entity.isFavorite,
            lastPlayed = entity.lastPlayed,
        )
    }

    fun mapToEntity(domain: Song): SongEntity {
        return SongEntity(
            id = domain.id,
            title = domain.title,
            artist = domain.artist,
            duration = domain.duration,
            filePath = domain.filePath,
            thumbnail = domain.thumbnail,
            isFavorite = domain.isFavorite,
            lastPlayed = domain.lastPlayed,
        )
    }
}