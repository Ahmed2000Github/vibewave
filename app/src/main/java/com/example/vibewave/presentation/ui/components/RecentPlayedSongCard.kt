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
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp


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

        SongThumbnail(
            thumbnail = song?.thumbnail,
            drawableId = song?.drawableThumbnail ?:R.drawable.album1,
            cardWidth = cardWidth
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


@Composable
fun RecentPlayedSongCardSkeleton() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = (screenWidth - 72.dp) / 2
    val infiniteTransition = rememberInfiniteTransition()

    // Animated alpha values for shimmer effect
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier.width(cardWidth)
    ) {
        // Image skeleton
        Box(
            modifier = Modifier
                .size(cardWidth)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Gray.copy(alpha = alpha))
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Title skeleton
        Box(
            modifier = Modifier
                .width(cardWidth * 0.8f)
                .height(18.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray.copy(alpha = alpha))
        )

        Spacer(modifier = Modifier.height(5.dp))

        // Artist skeleton
        Box(
            modifier = Modifier
                .width(cardWidth * 0.6f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray.copy(alpha = alpha))
        )
    }
}