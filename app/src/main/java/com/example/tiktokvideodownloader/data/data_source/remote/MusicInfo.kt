package com.example.tiktokvideodownloader.data.data_source.remote
import kotlinx.serialization.Serializable

@Serializable
data class MusicInfo(
    val album: String?=null,
    val author: String?=null,
    val cover: String?=null,
    val duration: Int?=null,
    val id: String?=null,
    val original: Boolean?=null,
    val play: String?=null,
    val title: String?=null
)