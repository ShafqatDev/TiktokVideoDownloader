package com.example.tiktokvideodownloader.domain.repository

import com.example.tiktokvideodownloader.data.data_source.remote.Tiktok
import com.example.tiktokvideodownloader.data.response.NetworkResponse

interface TiktokRepository {
    suspend fun analyzeTiktokVideo(url: String, hd: Int): NetworkResponse<Tiktok>
}