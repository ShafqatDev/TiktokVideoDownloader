package com.example.tiktokvideodownloader.domain.usecase

import com.example.tiktokvideodownloader.domain.repository.DownloadRepository

class DownloadAudioUseCase(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(url: String, fileName: String) {
        downloadRepository.downloadMP3(url, fileName)
    }
}