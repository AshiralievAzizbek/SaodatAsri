package me.owapps.saodatasri.util

import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import androidx.core.net.toUri
import me.owapps.saodatasri.data.entities.Raw

object Converters {
    fun fromRawToMediaItem(raw: Raw): MediaBrowserCompat.MediaItem {
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri((Constants.BASE_URL + raw.link).toUri())
            .setTitle(raw.soundName)
            .setSubtitle(raw.description)
            .setMediaId(raw._id)
            .build()
        return MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }

}
