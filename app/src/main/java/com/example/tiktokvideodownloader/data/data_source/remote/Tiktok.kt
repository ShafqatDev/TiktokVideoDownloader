package com.example.tiktokvideodownloader.data.data_source.remote

import kotlinx.serialization.Serializable

@Serializable
data class Tiktok(
    val code: Int?=null,
    val `data`: Data?=null,
    val msg: String?=null,
    val processed_time: Double?=null
)