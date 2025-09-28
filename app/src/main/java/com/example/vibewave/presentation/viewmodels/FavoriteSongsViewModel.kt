package com.example.vibewave.presentation.viewmodels
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.models.Song
import com.example.vibewave.domain.usecases.GetFavoriteSongsUseCase
import com.example.vibewave.domain.usecases.GetRecentlyPlayedSongsUseCase
import com.example.vibewave.domain.usecases.LoadDeviceSongsUseCase
import com.example.vibewave.presentation.state.FavoriteSongsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.N)
class FavoriteSongsViewModel @Inject constructor(
    private val getFavoriteSongsUseCase: GetFavoriteSongsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<FavoriteSongsState>(FavoriteSongsState.Loading)
    val state: StateFlow<FavoriteSongsState> = _state

    init {
        getFavoriteSongs()
    }
     fun refreshSongs() {
        getFavoriteSongs()
    }

    fun updateList(target: Song) {
        if (_state.value is FavoriteSongsState.Success) {
            val currentState = _state.value as FavoriteSongsState.Success
            val updatedSongs = currentState.songs.toMutableList().apply {
                removeIf { it.id == target.id } // or your condition to identify the song
            }
            _state.value = FavoriteSongsState.Success(updatedSongs)
        }
    }
    private fun getFavoriteSongs() {
        _state.value = FavoriteSongsState.Loading
        viewModelScope.launch {
            getFavoriteSongsUseCase()
                .catch { e -> _state.value = FavoriteSongsState.Error(e.message ?: "Unknown error") }
                .collect { songs ->
                    _state.value = FavoriteSongsState.Success(songs)
                }
        }
    }
}
