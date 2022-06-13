package me.owapps.saodatasri.di

import android.content.Context
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.owapps.saodatasri.data.remote.MediaSource
import me.owapps.saodatasri.exoplayer.MediaServiceConnection
import me.owapps.saodatasri.exoplayer.download.DownloadTracker
import me.owapps.saodatasri.repository.BooksRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMediaServiceConnection(@ApplicationContext context: Context) =
        MediaServiceConnection(context)

    @Provides
    @Singleton
    fun provideMediaSource(booksRepository: BooksRepository): MediaSource = MediaSource(booksRepository)


}