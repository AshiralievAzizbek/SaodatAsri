package me.owapps.saodatasri.exoplayer

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import me.owapps.saodatasri.data.remote.MediaSource
import me.owapps.saodatasri.exoplayer.callbacks.MediaPlaybackPreparer
import me.owapps.saodatasri.exoplayer.callbacks.MediaPlayerEventListener
import me.owapps.saodatasri.exoplayer.callbacks.MediaPlayerNotificationListener
import me.owapps.saodatasri.util.Constants.MEDIA_ROOT_ID
import me.owapps.saodatasri.util.Constants.NETWORK_ERROR
import javax.inject.Inject


private const val SERVICE_TAG = "PlayerService"

@AndroidEntryPoint
class PlayerService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var mediaSource: MediaSource

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(serviceJob)

    private lateinit var mediaSessionCompat: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaNotificationManager: MediaNotificationManager
    private lateinit var playerEventListener: MediaPlayerEventListener

    private var currentPlayingSong: MediaMetadataCompat? = null
    private var isPlayerInitialized = false

    var isForegroundService = false

    companion object {
        var currentSongDuration = 0L
            private set
    }

    override fun onCreate() {
        super.onCreate()


        serviceScope.launch(Dispatchers.Main) {
            mediaSource.fetchMediaData()
        }

        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, FLAG_IMMUTABLE)
        }

        mediaSessionCompat = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        sessionToken = mediaSessionCompat.sessionToken

        mediaNotificationManager = MediaNotificationManager(
            this, mediaSessionCompat.sessionToken,
            MediaPlayerNotificationListener(this)
        ) {
            currentSongDuration = exoPlayer.duration
        }

        val mediaPlaybackPreparer = MediaPlaybackPreparer(mediaSource) {
            currentPlayingSong = it
            preparePlayer(mediaSource.songs, it, true)
        }



        mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlaybackPreparer(mediaPlaybackPreparer)
        mediaSessionConnector.setPlayer(exoPlayer)

        playerEventListener = MediaPlayerEventListener(this)
        exoPlayer.addListener(playerEventListener)
        mediaNotificationManager.showNotification(exoPlayer)
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSessionCompat) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return mediaSource.songs[windowIndex].description
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val curSongIndex = if (currentPlayingSong == null) 0 else songs.indexOf(itemToPlay)
        Handler(Looper.getMainLooper()).post {
            exoPlayer.addMediaSource(mediaSource.asMediaSource(dataSourceFactory))
            exoPlayer.prepare()
            exoPlayer.seekTo(curSongIndex, 0L)
            exoPlayer.playWhenReady = playNow
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.removeListener(playerEventListener)
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        if (parentId == MEDIA_ROOT_ID) {
            val resultsSent = mediaSource.whenReady { isInitialized ->
                if (isInitialized) {
                    result.sendResult(mediaSource.asMediaItems())
                    if (!isPlayerInitialized && mediaSource.songs.isNotEmpty()) {
                        preparePlayer(mediaSource.songs, mediaSource.songs[0], false)
                        isPlayerInitialized = true
                    }
                } else {
                    mediaSessionCompat.sendSessionEvent(NETWORK_ERROR, null)
                    result.sendResult(null)
                }
            }
            if (!resultsSent) {
                result.detach()
            }
        }
    }
}
