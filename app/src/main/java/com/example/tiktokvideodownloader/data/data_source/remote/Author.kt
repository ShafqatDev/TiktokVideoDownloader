package com.example.tiktokvideodownloader.data.data_source.remote
import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val avatar: String?=null,
    val id: String?=null,
    val nickname: String?=null,
    val unique_id: String?=null
)