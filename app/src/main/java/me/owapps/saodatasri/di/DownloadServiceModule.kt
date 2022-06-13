package me.owapps.saodatasri.di

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import me.owapps.saodatasri.exoplayer.download.DownloadTracker
import me.owapps.saodatasri.util.Constants
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object DownloadServiceModule {


    private fun getDownloadDirectory(context: Context): File {
        var downloadDirectory: File? = null
        if (downloadDirectory == null) {
            downloadDirectory = context.getExternalFilesDir(null)
            if (downloadDirectory == null)
                downloadDirectory = context.filesDir
        }
        return downloadDirectory!!
    }

    @Provides
    @ServiceScoped
    fun provideDatabaseProvider(@ApplicationContext context: Context): StandaloneDatabaseProvider =
        StandaloneDatabaseProvider(context)

    @Provides
    @ServiceScoped
    fun provideHttpDataSourceFactory(): DefaultHttpDataSource.Factory =
        DefaultHttpDataSource.Factory()

    @Provides
    @ServiceScoped
    fun provideDownloadCache(
        @ApplicationContext context: Context,
        databaseProvider: StandaloneDatabaseProvider
    ): SimpleCache =
        SimpleCache(getDownloadDirectory(context), NoOpCacheEvictor(), databaseProvider)

    @Provides
    @ServiceScoped
    fun provideDownloadNotificationHelper(@ApplicationContext context: Context): DownloadNotificationHelper =
        DownloadNotificationHelper(
            context,
            Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID
        )

    @Provides
    @ServiceScoped
    fun provideDownloadTracker(
        @ApplicationContext context: Context,
        httpDataSourceFactory: DefaultHttpDataSource.Factory,
        downloadManager: DownloadManager
    ): DownloadTracker = DownloadTracker(context, httpDataSourceFactory, downloadManager)


    @Provides
    @ServiceScoped
    fun provideDownloadManager(
        @ApplicationContext context: Context,
        databaseProvider: StandaloneDatabaseProvider,
        downloadCache: SimpleCache,
        httpDataSourceFactory: DefaultHttpDataSource.Factory
    ): DownloadManager = DownloadManager(
        context,
        databaseProvider,
        downloadCache,
        httpDataSourceFactory,
        Executors.newFixedThreadPool( /* nThreads= */6)
    )

}