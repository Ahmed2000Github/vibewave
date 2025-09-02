package com.example.vibewave.utils

import android.annotation.SuppressLint

object FormatUtils {
    @SuppressLint("DefaultLocale")
    @JvmStatic
    public  fun formatTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }


}