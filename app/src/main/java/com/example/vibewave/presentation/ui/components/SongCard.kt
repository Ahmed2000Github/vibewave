package com.example.vibewave.presentation.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
fun SongCard(navController: NavController, song: Song?) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val cardWidth = 70.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.PlayMusic.createRoute(song!!))
            }
    ) {
        SongThumbnail(
            thumbnail = song?.thumbnail,
            cardWidth = cardWidth,
            drawableId = R.drawable.album1
        )

        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .height(cardWidth)
                .padding(vertical = 5.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = song?.title ?: "-",
                fontSize = 18.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song?.artist ?: "-",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = .4f)
            )
        }
        Box(
            modifier = Modifier
                .height(cardWidth)
        ) {
            Image(
                painter = painterResource(id = if (song?.isFavorite == true) R.drawable.heart_filled else R.drawable.heart),
                contentDescription = "favorite",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center)
            )
        }

    }


}