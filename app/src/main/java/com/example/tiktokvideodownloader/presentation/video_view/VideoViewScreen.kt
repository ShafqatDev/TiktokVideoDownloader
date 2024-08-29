package com.example.tiktokvideodownloader.presentation.video_view

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("OpaqueUnitKey")
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    uri: Uri,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val stopVideo = remember {
        mutableStateOf(false)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            Log.d("cvv", "Source: $source")
            Log.d("cvv", "Event: $event")
            Log.d("cvv", "--------------------")
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    stopVideo.value = false
                }

                Lifecycle.Event.ON_PAUSE -> {
                    stopVideo.value = true
                }

                else -> {

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItems(listOf(MediaItem.fromUri(uri)))
            playWhenReady = true
            prepare()
        }
    }
    LaunchedEffect(stopVideo.value) {
        withContext(Dispatchers.Main) {
            exoPlayer.playWhenReady = !stopVideo.value
        }
    }

    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
    if (!stopVideo.value) {
        exoPlayer.play()
    }

    DisposableEffect(
        key1 = AndroidView(modifier = modifier.fillMaxSize(), factory = {
            PlayerView(context).apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                useController = true
            }
        })
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}