package com.example.tiktokvideodownloader.presentation.tiktok

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tiktokvideodownloader.data.data_source.remote.Tiktok
import com.example.tiktokvideodownloader.data.response.NetworkResponse
import com.example.tiktokvideodownloader.presentation.components.LocalNavController
import com.example.tiktokvideodownloader.presentation.navigation.components.Screens
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiktokScreen() {
    val viewModel: TiktokViewModel = koinViewModel()
    val state by viewModel.tiktokState.collectAsState()
    val navController = LocalNavController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TikTok Video Downloader",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold, color = Color(0xFF333333)
            ),
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = state.videoUrl,
            onValueChange = { viewModel.updateVideoUrl(it) },
            label = { Text("Enter TikTok URL") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF333333),
                unfocusedBorderColor = Color(0xFF999999),
                cursorColor = Color(0xFF333333),
                focusedTextColor = Color(0xFF333333),
                unfocusedTextColor = Color(0xFF333333)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.analyzeVideo() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1DB954), contentColor = Color.White
            )
        ) {
            Text("Analyze Video", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val response = state.tiktok) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator(color = Color(0xFF1DB954))
            }

            is NetworkResponse.Success -> {
                val data = response.data
                data?.let {
                    TikTokDataDisplay(data, withOuWatermarkClick = {
                        viewModel.downloadVideoWithoutWatermark()
                    }, withWatermarkClick = {
                        viewModel.downloadVideoWithWatermark()
                    }, downloadMp3Click = {
                        viewModel.downloadAudio()
                    }, onPlayClick = {
                        navController.navigate(Screens.VideoViewScreen(url = it))
                    })
                }
            }

            is NetworkResponse.Failure -> {
                Text(
                    "Error: ${response.error}",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is NetworkResponse.Idle -> {
                Text(
                    "Enter video details to analyze.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TikTokDataDisplay(
    data: Tiktok,
    withOuWatermarkClick: () -> Unit = {},
    withWatermarkClick: () -> Unit = {},
    downloadMp3Click: () -> Unit = {},
    onPlayClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            GlideImage(
                model = data.data?.cover,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = { onPlayClick.invoke(data.data?.hdplay.toString()) },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = data.data?.title ?: "No Title",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold, color = Color(0xFF333333)
            ),
            maxLines = 3,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DownloadButton(label = "Download Without Watermark", onClick = {
            withOuWatermarkClick.invoke()
        })

        DownloadButton(label = "Download With Watermark", onClick = {
            withWatermarkClick.invoke()
        })

        DownloadButton(label = "Download MP3", onClick = {
            downloadMp3Click.invoke()
        })
    }
}

@Composable
fun DownloadButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1DB954), contentColor = Color.White
        )
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}
