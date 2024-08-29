package com.example.tiktokvideodownloader.data.repository

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.tiktokvideodownloader.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DownloadRepositoryImpl(
    private val context: Context
) : DownloadRepository {
    override suspend fun downloadVideo(url: String, fileName: String) {
        withContext(Dispatchers.IO) {
            val folder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Photos"
            )
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val newFileName = fileName + System.currentTimeMillis().toString()
            val request = DownloadManager.Request(Uri.parse(url)).setTitle(newFileName)
                .setDescription("Downloading Video...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true).setAllowedOverRoaming(true).setMimeType("video/mp4")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, newFileName)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }
        Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show()
    }

    override suspend fun downloadMP3(url: String, fileName: String) {
        withContext(Dispatchers.IO) {
            val folder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Photos"
            )
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val newFileName = fileName + System.currentTimeMillis().toString()
            val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url)).setTitle(newFileName)
                .setDescription("Downloading Audio...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true).setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, newFileName)
                .setMimeType("audio/mp3")
            downloadManager.enqueue(request)

        }
        Toast.makeText(context, "Audio Download Started", Toast.LENGTH_SHORT).show()
    }
}