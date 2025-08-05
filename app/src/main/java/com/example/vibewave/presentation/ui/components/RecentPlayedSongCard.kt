package com.example.vibewave.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.navigation.Screen

@Composable
fun RecentPlayedSongCard(navController: NavController, song: Song?) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val cardWidth = (screenWidth - (72.dp))/2
    Column(
        modifier = Modifier.width(cardWidth)
            .clickable{
                navController.navigate(Screen.PlayMusic.createRoute(song!!))
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.album1),
            contentDescription = "App logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(cardWidth)
                .shadow(
                    elevation = 10.dp, // Shadow intensity
                    shape = RoundedCornerShape(20.dp), // Match your clip shape
                    spotColor = Color.White.copy(alpha = 0.8f) // Optional shadow color
                )
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = song?.title ?: "-",
            fontSize = 18.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = song?.artist ?: "-",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = .4f)
        )
    }


}