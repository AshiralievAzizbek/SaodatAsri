package me.owapps.saodatasri.exoplayer.download

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import me.owapps.saodatasri.R
import me.owapps.saodatasri.util.Constants.DOWNLOAD_FOREGROUND_NOTIFICATION_ID
import me.owapps.saodatasri.util.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import me.owapps.saodatasri.util.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_NAME
import javax.inject.Inject

@AndroidEntryPoint
class MyDownloadService : DownloadService(
    DOWNLOAD_FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
) {
    companion object {
        fun getJavaClass() = MyDownloadService::class.java
    }

    @Inject
    lateinit var mDownloadManager: DownloadManager

    @Inject
    lateinit var downloadNotificationHelper: DownloadNotificationHelper

    override fun getDownloadManager(): DownloadManager {

        return mDownloadManager
    }

    override fun getScheduler(): Scheduler? = null
    
    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                    DOWNLOAD_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        return downloadNotificationHelper.buildProgressNotification(
            this,
            R.drawable.ic_baseline_download,
            null,
            null,
            downloads,
            notMetRequirements
        )
    }
}