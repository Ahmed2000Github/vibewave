package com.example.vibewave.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.example.vibewave.data.local.database.AppDatabase
import com.example.vibewave.data.local.database.dao.SongDao
import com.example.vibewave.data.local.mediasource.DeviceMusicSource
import com.example.vibewave.data.mappers.DeviceMusicMapper
import com.example.vibewave.data.mappers.SongMapper
import com.example.vibewave.data.repositories.SongsRepositoryImpl
import com.example.vibewave.domain.repositories.SongsRepository
import com.example.vibewave.domain.usecases.GetAllSongsUseCase
import com.example.vibewave.domain.usecases.GetRecentlyPlayedSongsUseCase
import com.example.vibewave.domain.usecases.LoadDeviceSongsUseCase
import com.example.vibewave.domain.usecases.ToggleSongFavoriteUseCase
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
    @Provides
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun provideDeviceMusicSource(
        contentResolver: ContentResolver
    ): DeviceMusicSource {
        return DeviceMusicSource(contentResolver)
    }
}
@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Provides
    fun provideSongMapper(): SongMapper {
        return SongMapper()
    }
    @Provides
    fun provideDeviceMusicMapper(): DeviceMusicMapper {
        return DeviceMusicMapper()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideRecentlyPlayedRepository(
        dao: SongDao,
        deviceMusicSource: DeviceMusicSource,
        songMapper: SongMapper,
        deviceMusicMapper: DeviceMusicMapper
    ): SongsRepository {
        return SongsRepositoryImpl(deviceMusicSource,dao,songMapper,deviceMusicMapper)
    }

    @Provides
    fun provideGetRecentlyPlayedSongsUseCase(
        repository: SongsRepository
    ): GetRecentlyPlayedSongsUseCase {
        return GetRecentlyPlayedSongsUseCase(repository)
    }
    @Provides
    fun provideLoadDeviceSongsUseCase(
        repository: SongsRepository
    ): LoadDeviceSongsUseCase {
        return LoadDeviceSongsUseCase(repository)
    }
    @Provides
    fun provideGetAllSongsUseCase(
        repository: SongsRepository
    ): GetAllSongsUseCase {
        return GetAllSongsUseCase(repository)
    }
    @Provides
    fun provideToggleSongFavoriteUseCase(
        repository: SongsRepository
    ): ToggleSongFavoriteUseCase {
        return ToggleSongFavoriteUseCase(repository)
    }
}