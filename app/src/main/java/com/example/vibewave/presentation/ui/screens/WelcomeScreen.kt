package com.example.vibewave.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibewave.R
import com.example.vibewave.presentation.ui.components.GradientButton
import com.example.vibewave.presentation.ui.components.RadialGradient


@Composable
fun WelcomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.TopStart) {

            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(Color(0x555E46F8), Color(0x00000000)),
                        Offset(x = screenWidth.value - 200, y = screenHeight.value - 300),
                        radius = 200f
                    )
                )
            )

            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x55C03EFE), Color(0x00000000)),
                        Offset(x = screenWidth.value - 100, y = screenHeight.value - 200),
                        radius = 200f
                    )
                )
            )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp),contentAlignment = Alignment.Center) {
                RadialGradient(
                    colors = listOf( Color(0x225E46F8),Color(0x00000000)),
                    radius = 200f
                )
                Image(
                    painter = painterResource(id = R.drawable.casque),
                    contentDescription = "App logo",
                    modifier = Modifier.size(250.dp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Getting started",
                color = Color(0xFFFFFFFF),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Getting started Getting",
                color = Color(0xFFFFFFFF)
            )
            Spacer(modifier = Modifier.height(32.dp))
            GradientButton({
                navController.navigate("home")
//                {
//                    popUpTo("welcome") { inclusive = true }
//                }
            })
            Spacer(modifier = Modifier.height(100.dp))
        }
        }
    }
}

