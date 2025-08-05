package com.example.vibewave.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibewave.R

@Composable
fun GradientButton( onClick: () -> Unit) {
    val gradientColors = listOf(
        Color(0xFF5E46F8),
        Color(0xFFA284F6)
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth(fraction = .5f)
            .padding(0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = androidx.compose.ui.geometry.Offset.Zero,
                        end = androidx.compose.ui.geometry.Offset.Infinite
                    ),
                    shape = CircleShape
                )
                .padding(horizontal = 40.dp, vertical = 20.dp),

            ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "Lets Go  ",
                    color = Color.White,
                    fontSize = 20.sp,
                )
                Image(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "App logo",
                    modifier = Modifier.size(15.dp)
                )
            }

        }
    }
}
