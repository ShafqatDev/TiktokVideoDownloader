package com.example.tiktokvideodownloader.domain.repository

interface DownloadRepository {
    suspend fun downloadVideo(url: String, fileName: String)
    suspend fun downloadMP3(url: String, fileName: String)
}