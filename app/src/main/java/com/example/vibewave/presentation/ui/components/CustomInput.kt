package com.example.vibewave.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import com.example.vibewave.R

@Composable
fun CustomInput(
    label: String = "Email",
    placeholder: String = "search song"
) {
    val text = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text.value,
        singleLine = true,
        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
        onValueChange = { text.value = it },
        placeholder = { Text(placeholder,color = Color.Gray.copy(alpha = 0.7f)) },
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
             unfocusedContainerColor  = Color(0xFF1D1725),
             focusedIndicatorColor = Color.Transparent,
             unfocusedIndicatorColor = Color.Transparent,
             disabledIndicatorColor = Color.Transparent
         ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    keyboardController?.hide()
                }
            }

    )
}