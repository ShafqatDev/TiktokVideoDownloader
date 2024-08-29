package com.example.tiktokvideodownloader.domain.usecase

import com.example.tiktokvideodownloader.data.data_source.remote.Tiktok
import com.example.tiktokvideodownloader.data.response.NetworkResponse
import com.example.tiktokvideodownloader.domain.repository.TiktokRepository

class AnalyzeTiktokVideoUseCase(private val repository: TiktokRepository) {

    suspend operator fun invoke(url: String, hd: Int): NetworkResponse<Tiktok> {
        return repository.analyzeTiktokVideo(url, hd)
    }
}
