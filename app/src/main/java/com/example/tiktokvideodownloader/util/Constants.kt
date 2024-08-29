package com.example.tiktokvideodownloader.util

import android.os.Build
import androidx.annotation.RequiresApi

object Constants {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    val permissionList =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                android.Manifest.permission.INTERNET
            )
        } else {
            listOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.INTERNET
            )
        }
}