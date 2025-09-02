package com.example.vibewave.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.vibewave.R


@Composable
fun GradientPlayPauseButton(
    isPlaying: Boolean,
    iconSize: Dp = 30.dp,
    iconLeftPadding: Dp = 5.dp,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF7E5EDA), Color(0xFFAC8FFD)),
        start = Offset(0f, 50f),
        end = Offset(50f, 0f)
    )

    Box(
        modifier = Modifier .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        RadialGradient(
            colors = listOf( Color(0x77AE92FF),Color(0x00000000)),
            radius = 35f
        )
        Box(
            modifier = modifier
                .size(70.dp)
                .background(gradient, CircleShape)
                .padding(start = if (isPlaying) 0.dp else iconLeftPadding)
                .clickable(onClick = onToggle),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                contentDescription = if (isPlaying) "Pause" else "Play",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}