package com.example.vibewave.presentation.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.presentation.state.SearchSongState
import com.example.vibewave.presentation.viewmodels.FavoriteSongsViewModel
import com.example.vibewave.presentation.viewmodels.SearchSongViewModel

@Composable
@RequiresApi(Build.VERSION_CODES.N)
fun CustomInput(
    label: String = "Email",
    placeholder: String = "search song",
    searchSongViewModel: SearchSongViewModel,
    navController: NavController,
    favoriteSongsViewModel: FavoriteSongsViewModel,
) {
    val text = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val filteredSongsState by searchSongViewModel.state.collectAsState()


    Column {
        Spacer(Modifier.height(15.dp))
        TextField(
            value = text.value,
            singleLine = true,
            textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
            onValueChange = {
                text.value = it
                searchSongViewModel.searchSong(it)
            },
            placeholder = { Text(placeholder, color = Color.Gray.copy(alpha = 0.7f)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "App logo",
                    modifier = Modifier.size(15.dp)
                )
            },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = Color(0xFF1D1725),
                unfocusedContainerColor = Color(0xFF1D1725),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        keyboardController?.hide()
                    }
                }

        )
        Box {
            when (val state = filteredSongsState) {
                is SearchSongState.Success -> {
                    if (text.value.isNotEmpty() && state.songs.isNotEmpty()) Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF1D1725),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .heightIn(max = 200.dp)
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(15.dp)
                            )
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(30.dp)
                        ) {
                            items(state.songs.size) { index ->
                                SongCard(
                                    navController = navController,
                                    initialSong = state.songs[index],
                                    favoriteSongsViewModel = favoriteSongsViewModel
                                )
                            }
                        }
                    }
                }

                is SearchSongState.Error -> {
                }

                else -> {
                }
            }
        }
    }
}