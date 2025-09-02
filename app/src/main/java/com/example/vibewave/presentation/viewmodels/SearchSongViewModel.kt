package com.example.vibewave.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibewave.domain.usecases.SearchSongUseCase
import com.example.vibewave.presentation.state.SearchSongState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSongViewModel @Inject constructor(
    private val searchSongUseCase: SearchSongUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<SearchSongState>(SearchSongState.Loading)
    val state: StateFlow<SearchSongState> = _state


    fun searchSong(query:String) {
        viewModelScope.launch {
            searchSongUseCase(query)
                .catch { e -> _state.value = SearchSongState.Error(e.message ?: "Unknown error") }
                .collect { songs ->
                    _state.value = SearchSongState.Success(songs)
                }
        }
    }
}
