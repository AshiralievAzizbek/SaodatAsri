package me.owapps.saodatasri.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import me.owapps.saodatasri.R
import me.owapps.saodatasri.util.Constants.NOTIFICATION_CHANNEL_ID
import me.owapps.saodatasri.util.Constants.NOTIFICATION_ID

class MediaNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val newAudioCallback: () -> Unit
) {

    private val mediaController = MediaControllerCompat(context, sessionToken)
    private val notificationManager: PlayerNotificationManager =
        PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
            .setMediaDescriptionAdapter(Adapter(mediaController))
            .setChannelNameResourceId(R.string.notification_channel_name)
            .setChannelDescriptionResourceId(R.string.notification_channel_description)
            .setNotificationListener(notificationListener)
            .setSmallIconResourceId(R.drawable.ic_baseline_music_note_24)
            .build()

    fun showNotification(player: Player){
        notificationManager.setPlayer(player)
    }

    inner class Adapter(private val mediaController: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence =
            mediaController.metadata.description.toString()

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            mediaController.sessionActivity

        override fun getCurrentContentText(player: Player): CharSequence =
            mediaController.metadata.description.subtitle.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? = null

    }


}