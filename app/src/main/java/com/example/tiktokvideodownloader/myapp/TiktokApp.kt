package com.example.tiktokvideodownloader.myapp

import android.app.Application
import com.example.tiktokvideodownloader.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TiktokApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TiktokApp)
            modules(appModule)
        }
    }
}