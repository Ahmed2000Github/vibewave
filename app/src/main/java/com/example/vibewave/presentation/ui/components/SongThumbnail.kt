package com.example.vibewave.presentation.ui.components

import android.graphics.Bitmap
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.domain.models.Song
import com.example.vibewave.presentation.navigation.Screen

@Composable
fun SongThumbnail(thumbnail: ByteArray?,cardWidth: Dp,drawableId: Int = R.drawable.album1) {
    thumbnail?.let { byteArray ->
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Song thumbnail",
            modifier = Modifier.size(cardWidth),
            contentScale = ContentScale.Crop
        )
    } ?:Image(
    painter = painterResource(id = drawableId ),
    contentDescription = "Song thumbnail",
    contentScale = ContentScale.Crop,
    modifier = Modifier
    .size(cardWidth)
    .shadow(
    elevation = 10.dp,
    shape = RoundedCornerShape(20.dp),
    spotColor = Color.White.copy(alpha = 0.8f)
    )
    .clip(RoundedCornerShape(20.dp))
    )
}

