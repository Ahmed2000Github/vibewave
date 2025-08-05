package com.example.vibewave.di

import android.content.Context
import androidx.room.Room
import com.example.vibewave.data.local.AppDatabase
import com.example.vibewave.data.local.dao.SongDao
import com.example.vibewave.data.mappers.SongMapper
import com.example.vibewave.data.repositories.SongsRepositoryImpl
import com.example.vibewave.domain.repositories.SongsRepository
import com.example.vibewave.domain.usecases.GetRecentlyPlayedSongsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "music-db"
        ).build()
    }

    @Provides
    fun provideSongDao(database: AppDatabase): SongDao {
        return database.songDao()
    }
}
@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Provides
    fun provideSongMapper(): SongMapper {
        return SongMapper()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideRecentlyPlayedRepository(
        dao: SongDao,
        mapper: SongMapper
    ): SongsRepository {
        return SongsRepositoryImpl(dao, mapper)
    }

    @Provides
    fun provideGetRecentlyPlayedSongsUseCase(
        repository: SongsRepository
    ): GetRecentlyPlayedSongsUseCase {
        return GetRecentlyPlayedSongsUseCase(repository)
    }
}