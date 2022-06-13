package me.owapps.saodatasri.data.remote

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.owapps.saodatasri.data.entities.Raw
import me.owapps.saodatasri.data.remote.State.*
import me.owapps.saodatasri.repository.BooksRepository
import me.owapps.saodatasri.util.Constants
import javax.inject.Inject

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}

class MediaSource @Inject constructor(private val booksRepository: BooksRepository) {

    var songs = emptyList<MediaMetadataCompat>()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == STATE_CREATED || state == STATE_INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == STATE_INITIALIZED)
            true
        }
    }

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = STATE_INITIALIZING
        val allSongs = getAllSongs()
        songs = allSongs.map { raw ->
            Builder()
                .putString(METADATA_KEY_ARTIST, raw.soundName)
                .putString(METADATA_KEY_MEDIA_ID, raw._id)
                .putString(METADATA_KEY_TITLE, raw.soundName)
                .putString(METADATA_KEY_DISPLAY_TITLE, raw.description)
                .putString(METADATA_KEY_MEDIA_URI, Constants.BASE_URL + raw.link)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, raw.description)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, raw.description)
                .build()
        }

        state = STATE_INITIALIZED
    }

    private suspend fun getAllSongs(): ArrayList<Raw> {
        val songs = arrayListOf<Raw>()
        val response = booksRepository.getBooks()
        if (response.data != null) {
            for (book in response.data.data)
                songs.addAll(book.raws)
        }
        return songs
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach { song ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()


}

