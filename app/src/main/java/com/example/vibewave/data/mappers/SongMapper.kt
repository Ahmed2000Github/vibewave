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
            uri = entity.uri,
            timestamp = entity.timestamp,
        )
    }

    fun mapToEntity(domain: Song): SongEntity {
        return SongEntity(
            id = domain.id,
            title = domain.title,
            artist = domain.artist,
            duration = domain.duration,
            uri = domain.uri,
            timestamp = domain.timestamp,
        )
    }
}