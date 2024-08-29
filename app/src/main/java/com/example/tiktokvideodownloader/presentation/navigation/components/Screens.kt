package com.example.tiktokvideodownloader.presentation.navigation.components

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object TiktokScreen:Screens()
    @Serializable
    data class VideoViewScreen(val url:String):Screens()
}