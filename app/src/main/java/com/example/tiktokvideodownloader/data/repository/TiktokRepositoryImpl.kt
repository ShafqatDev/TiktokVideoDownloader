package com.example.tiktokvideodownloader.data.repository

import com.example.tiktokvideodownloader.data.controller.NetworkClient
import com.example.tiktokvideodownloader.data.data_source.remote.Tiktok
import com.example.tiktokvideodownloader.data.response.NetworkResponse
import com.example.tiktokvideodownloader.data.response.RequestTypes
import com.example.tiktokvideodownloader.domain.repository.TiktokRepository

class TiktokRepositoryImpl : TiktokRepository {
    override suspend fun analyzeTiktokVideo(url: String, hd: Int): NetworkResponse<Tiktok> {
        val videoUrl =
            "https://www.tikwm.com/api/?url=$url&hd=$hd"
        return NetworkClient.makeNetworkRequest<Tiktok>(
            url = videoUrl,
            requestType = RequestTypes.Get,
        )
    }
}