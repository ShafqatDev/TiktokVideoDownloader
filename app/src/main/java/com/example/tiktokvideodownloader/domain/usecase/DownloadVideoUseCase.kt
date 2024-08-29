package com.example.tiktokvideodownloader.domain.usecase

import com.example.tiktokvideodownloader.domain.repository.DownloadRepository

class DownloadVideoUseCase(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(url: String, fileName: String) {
        downloadRepository.downloadVideo(url, fileName)
    }
}
