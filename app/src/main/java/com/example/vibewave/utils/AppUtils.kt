package com.example.vibewave.utils

import com.example.vibewave.R

object AppUtils {

    @JvmStatic
    public  fun getRandomImage(): Int {
        val images: List<Int> = listOf(
            R.drawable.album1,
            R.drawable.album2,
            R.drawable.album3,
            R.drawable.album4,
            R.drawable.album5,
            R.drawable.album6,
            R.drawable.album7,
            R.drawable.album8,
        )
     return images.random()
    }

}