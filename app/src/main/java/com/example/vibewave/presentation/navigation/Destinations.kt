package com.example.vibewave.presentation.navigation
import android.net.Uri
import com.example.vibewave.domain.models.Song
import com.google.gson.Gson
import kotlinx.serialization.Serializable


@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Welcome : Screen("welcome")

    @Serializable
    object Home : Screen("home")

    @Serializable
    object MusicList : Screen("musicList")

    @Serializable
    object PlayMusic : Screen("play_music/{song}") {
        fun createRoute(song: Song?) = "play_music/${Uri.encode(Gson().toJson(song))}"
    }
}