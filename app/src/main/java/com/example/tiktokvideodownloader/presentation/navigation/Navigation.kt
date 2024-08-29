package com.example.tiktokvideodownloader.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.tiktokvideodownloader.presentation.navigation.components.Screens.*
import com.example.tiktokvideodownloader.presentation.tiktok.TiktokScreen
import com.example.tiktokvideodownloader.presentation.video_view.VideoPlayer

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = TiktokScreen) {
        composable<TiktokScreen> {
            TiktokScreen()
        }
        composable<VideoViewScreen> {
            val url = it.toRoute<VideoViewScreen>().url.toUri()
            VideoPlayer(uri = url)
        }
    }
}