package com.example.vibewave.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.usecases.ToggleSongFavoriteUseCase
import com.example.vibewave.domain.usecases.UpdateLastPlayUseCase
import com.example.vibewave.presentation.state.SongCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.N)

class SongViewModel @Inject constructor(
    private val toggleSongFavoriteUseCase: ToggleSongFavoriteUseCase,
    private val updateLastPlayUseCase: UpdateLastPlayUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<SongCardState>(SongCardState.Loading)
    val state: StateFlow<SongCardState> = _state
    private var _favoriteSongsViewModel: FavoriteSongsViewModel? = null
    private var _recentlyPlayedViewModel: RecentlyPlayedViewModel? = null


    fun setFavoriteSongsViewModel(favoriteSongsViewModel:FavoriteSongsViewModel?) {

        _favoriteSongsViewModel = favoriteSongsViewModel
    }
    fun setRecentlyPlayedViewModel(recentlyPlayedViewModel:RecentlyPlayedViewModel) {
        _recentlyPlayedViewModel = recentlyPlayedViewModel
    }
     fun toggleSongFavorite(songId:String) {
        viewModelScope.launch {
            toggleSongFavoriteUseCase(songId)
                    .catch { e ->
                        _state.value = SongCardState.Error(e.message ?: "Unknown error") }
                    .collect { song ->
                        _state.value = SongCardState.Success(song)
                        _favoriteSongsViewModel?.updateList(song)
                    }
        }
    }
    fun updateLastPlay(songId:String) {
        viewModelScope.launch {
            updateLastPlayUseCase(songId)
                .catch { e ->
                    _state.value = SongCardState.Error(e.message ?: "Unknown error") }
                .collect { song ->
                    _state.value = SongCardState.Success(song)
                    _recentlyPlayedViewModel?.refreshSongs()
                }
        }
    }

}