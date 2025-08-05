package com.example.vibewave.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RadialGradient(colors:List<Color>, center: Offset = Offset.Unspecified, radius: Float = 150f  ) {

    val gradient = Brush.radialGradient(
        colors = colors,
        center = center,
        radius = radius
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(fraction = 1f)
            .height(300.dp)
            .background(gradient)
    )
}