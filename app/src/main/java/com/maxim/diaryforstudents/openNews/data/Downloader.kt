package com.maxim.diaryforstudents.openNews.data

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import javax.inject.Inject

interface Downloader {
    fun download(fileName: String, url: String): Long

    class Base @Inject constructor(context: Context): Downloader {
        private val downloadManager = context.getSystemService(DownloadManager::class.java)

        override fun download(fileName: String, url: String): Long {
            val request = DownloadManager.Request(url.trim().toUri())
                .setMimeType("application/vnd.android.package-archive")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            return downloadManager.enqueue(request)
        }
    }
}