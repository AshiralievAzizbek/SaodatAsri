package me.owapps.saodatasri.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.owapps.saodatasri.data.entities.Book
import me.owapps.saodatasri.data.entities.Raw
import me.owapps.saodatasri.exoplayer.MediaServiceConnection
import me.owapps.saodatasri.exoplayer.isPlayEnabled
import me.owapps.saodatasri.exoplayer.isPlaying
import me.owapps.saodatasri.exoplayer.isPrepared
import me.owapps.saodatasri.repository.BooksRepository
import me.owapps.saodatasri.util.Constants.MEDIA_ROOT_ID
import me.owapps.saodatasri.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val mediaServiceConnection: MediaServiceConnection
) :
    ViewModel() {


    private val _books = MutableLiveData<List<Book>>()
    val mBooks: LiveData<List<Book>> get() = _books
    private val _mediaItems: MutableLiveData<Resource<List<Raw>>> =
        MutableLiveData<Resource<List<Raw>>>()
    val mediaItems: LiveData<Resource<List<Raw>>> get() = _mediaItems

    val isConnected = mediaServiceConnection.isConnected
    val networkError = mediaServiceConnection.networkError
    val currentPlayingSong = mediaServiceConnection.currentPlayingSong
    val playbackState = mediaServiceConnection.playbackState

    fun fetchBooks() {
        viewModelScope.launch {
            val response = booksRepository.getBooks()
            if (response.data != null)
                _books.value = response.data.data
        }

    }


    init {
        _mediaItems.postValue(Resource.Loading())
        mediaServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map {
                        Raw(
                            _id = it.mediaId!!,
                            soundName = it.description.title.toString(),
                            description = it.description.subtitle.toString(),
                            link = it.description.mediaUri.toString(),
                            position = children.indexOf(it) + 1
                        )
                    }
                    _mediaItems.postValue(Resource.Success(items))
                }
            })
    }

    fun seekTo(position: Long) {
        mediaServiceConnection.transportControls.seekTo(position)
    }

    fun skipToPrevious() {
        mediaServiceConnection.transportControls.skipToPrevious()
    }

    fun playOrToggle(mediaItem: Raw, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        Log.d("XXXXX", "playOrToggle: clicked ${playbackState.value} ")
        if (isPrepared && mediaItem._id == currentPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackStateCompat ->
                when {
                    playbackStateCompat.isPlaying -> {
                        if (toggle) mediaServiceConnection.transportControls.pause()
                    }
                    playbackStateCompat.isPlayEnabled -> {
                        mediaServiceConnection.transportControls.play()
                    }
                    else -> Unit
                }
            }

        } else {
            mediaServiceConnection.transportControls.playFromMediaId(mediaItem._id, null)
        }
    }

    fun skipToNext() {
        mediaServiceConnection.transportControls.skipToNext()
    }

    override fun onCleared() {
        super.onCleared()
        mediaServiceConnection.unSubscribe(MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }

}