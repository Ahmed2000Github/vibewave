package com.example.vibewave.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vibewave.data.local.database.dao.SongDao
import com.example.vibewave.data.local.entities.SongEntity

@Database(
    entities = [SongEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}