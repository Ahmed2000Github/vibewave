package com.example.vibewave.presentation.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
