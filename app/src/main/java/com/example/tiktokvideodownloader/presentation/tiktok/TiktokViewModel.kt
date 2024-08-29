package com.example.tiktokvideodownloader.presentation.tiktok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktokvideodownloader.data.data_source.remote.Tiktok
import com.example.tiktokvideodownloader.data.response.NetworkResponse
import com.example.tiktokvideodownloader.domain.usecase.AnalyzeTiktokVideoUseCase
import com.example.tiktokvideodownloader.domain.usecase.DownloadAudioUseCase
import com.example.tiktokvideodownloader.domain.usecase.DownloadVideoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TiktokState(
    val tiktok: NetworkResponse<Tiktok> = NetworkResponse.Idle(),
    val videoUrl: String = "",
    val hd: String = "1"
)

class TiktokViewModel(
    private val analyzeTiktokVideoUseCase: AnalyzeTiktokVideoUseCase,
    private val downloadAudioUseCase: DownloadAudioUseCase,
    private val downloadVideoUseCase: DownloadVideoUseCase
) : ViewModel() {

    private val _tiktokState = MutableStateFlow(TiktokState())
    val tiktokState: StateFlow<TiktokState> = _tiktokState.asStateFlow()

    fun updateVideoUrl(url: String) {
        _tiktokState.update { it.copy(videoUrl = url) }
    }

    fun analyzeVideo() {
        val url = _tiktokState.value.videoUrl
        val hd = _tiktokState.value.hd.toIntOrNull() ?: 0
        if (url.isNotBlank()) {
            _tiktokState.update { it.copy(tiktok = NetworkResponse.Loading()) }

            viewModelScope.launch {
                val result = analyzeTiktokVideoUseCase(url, hd)
                _tiktokState.update { it.copy(tiktok = result) }
            }
        }
    }

    fun downloadVideoWithoutWatermark() {
        val url = _tiktokState.value.tiktok.data?.data?.hdplay.toString()
        val title = _tiktokState.value.tiktok.data?.data?.title
        if (title != null) {
            viewModelScope.launch {
                downloadVideoUseCase(url, title)
            }
        }
    }

    fun downloadVideoWithWatermark() {
        val url = _tiktokState.value.tiktok.data?.data?.wmplay.toString()
        val title = _tiktokState.value.tiktok.data?.data?.title
        if (title != null) {
            viewModelScope.launch {
                downloadVideoUseCase(url, title)
            }
        }
    }

    fun downloadAudio() {
        val url = _tiktokState.value.tiktok.data?.data?.music
        val title = _tiktokState.value.tiktok.data?.data?.title
        if (title != null) {
            viewModelScope.launch {
                downloadAudioUseCase(url.toString(), title)
            }
        }
    }
}