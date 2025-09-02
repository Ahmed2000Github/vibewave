package com.example.vibewave.di

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.vibewave.data.local.database.AppDatabase
import com.example.vibewave.data.local.database.dao.SongDao
import com.example.vibewave.data.local.mediasource.DeviceMusicSource
import com.example.vibewave.data.mappers.DeviceMusicMapper
import com.example.vibewave.data.mappers.SongMapper
import com.example.vibewave.data.repositories.SongsRepositoryImpl
import com.example.vibewave.domain.repositories.SongsRepository
import com.example.vibewave.domain.usecases.GetAllSongsUseCase
import com.example.vibewave.domain.usecases.GetFavoriteSongsUseCase
import com.example.vibewave.domain.usecases.GetRecentSongUseCase
import com.example.vibewave.domain.usecases.GetRecentlyPlayedSongsUseCase
import com.example.vibewave.domain.usecases.LoadDeviceSongsUseCase
import com.example.vibewave.domain.usecases.LoadSongsUseCase
import com.example.vibewave.domain.usecases.SearchSongUseCase
import com.example.vibewave.domain.usecases.ToggleSongFavoriteUseCase
import com.example.vibewave.domain.usecases.UpdateLastPlayUseCase
import com.example.vibewave.services.AudioPlayerController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import linc.com.amplituda.Amplituda

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

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    fun provideAudioPlayerController(@ApplicationContext context: Context): AudioPlayerController {
        return AudioPlayerController(context);
    }

    @Provides
    fun provideAmplituda(@ApplicationContext context: Context): Amplituda {
        return Amplituda(context);
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
@RequiresApi(Build.VERSION_CODES.O)
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
    fun provideGetRecentSongUseCase(
        repository: SongsRepository
    ): GetRecentSongUseCase {
        return GetRecentSongUseCase(repository)
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
    fun provideGetFavoriteSongsUseCase(
        repository: SongsRepository
    ): GetFavoriteSongsUseCase {
        return GetFavoriteSongsUseCase(repository)
    }

    @Provides
    fun provideToggleSongFavoriteUseCase(
        repository: SongsRepository
    ): ToggleSongFavoriteUseCase {
        return ToggleSongFavoriteUseCase(repository)
    }
    @Provides
    fun provideSearchSongUseCase(
        repository: SongsRepository
    ): SearchSongUseCase {
        return SearchSongUseCase(repository)
    }

    @Provides
    fun provideUpdateRecentPlayUseCase(
        repository: SongsRepository
    ): UpdateLastPlayUseCase {
        return UpdateLastPlayUseCase(repository)
    }
}

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @RequiresApi(Build.VERSION_CODES.O)
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
    fun provideLoadSongsUseCase(
        repository: SongsRepository
    ): LoadSongsUseCase {
        return LoadSongsUseCase(repository)
    }
}