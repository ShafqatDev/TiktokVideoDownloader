package com.example.tiktokvideodownloader.di

import android.annotation.SuppressLint
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.example.tiktokvideodownloader.data.repository.DownloadRepositoryImpl
import com.example.tiktokvideodownloader.data.repository.TiktokRepositoryImpl
import com.example.tiktokvideodownloader.domain.repository.DownloadRepository
import com.example.tiktokvideodownloader.domain.repository.TiktokRepository
import com.example.tiktokvideodownloader.domain.usecase.AnalyzeTiktokVideoUseCase
import com.example.tiktokvideodownloader.domain.usecase.DownloadAudioUseCase
import com.example.tiktokvideodownloader.domain.usecase.DownloadVideoUseCase
import com.example.tiktokvideodownloader.presentation.tiktok.TiktokViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
val appModule = module {
    factory<TiktokRepository> {
        TiktokRepositoryImpl()
    }
    factory<DownloadRepository> {
        DownloadRepositoryImpl(get())
    }
    single {
        DownloadAudioUseCase(get())
    }
    single {
        DownloadVideoUseCase(get())
    }
    single {
        AnalyzeTiktokVideoUseCase(get())
    }
    viewModel {
        TiktokViewModel(get(), get(), get())
    }
    single<Player> {
        ExoPlayer.Builder(get()).setAudioAttributes(get(), true).setHandleAudioBecomingNoisy(true)
            .setTrackSelector(DefaultTrackSelector(get())).build()
    }

    factory<MediaSession> {
        MediaSession.Builder(get(), get()).build()
    }
    single<AudioAttributes> {
        AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA)
            .build()
    }
}