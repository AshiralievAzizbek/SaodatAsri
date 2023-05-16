package me.owapps.saodatasri.ui.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.owapps.saodatasri.data.entities.BookX
import me.owapps.saodatasri.data.entities.book.Audio
import me.owapps.saodatasri.exoplayer.MediaServiceConnection
import me.owapps.saodatasri.exoplayer.isPlayEnabled
import me.owapps.saodatasri.exoplayer.isPlaying
import me.owapps.saodatasri.exoplayer.isPrepared
import me.owapps.saodatasri.repository.BooksRepository
import me.owapps.saodatasri.util.Constants.MEDIA_ROOT_ID
import me.owapps.saodatasri.util.Resource
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val mediaServiceConnection: MediaServiceConnection
) :
    ViewModel() {


    private val _books = MutableLiveData<List<BookX>>()
    val mBooks: LiveData<List<BookX>> get() = _books
    private val _mediaItems: MutableLiveData<Resource<List<Audio>>> =
        MutableLiveData<Resource<List<Audio>>>()
    val mediaItems: LiveData<Resource<List<Audio>>> get() = _mediaItems

    val isConnected = mediaServiceConnection.isConnected
    val networkError = mediaServiceConnection.networkError
    val currentPlayingSong = mediaServiceConnection.currentPlayingSong
    val playbackState = mediaServiceConnection.playbackState

    private val _currentMediaItems = MutableLiveData<List<Audio>>()
    val currentMediaItems: LiveData<List<Audio>> get() = _currentMediaItems

    fun fetchBooks() {
        viewModelScope.launch {
            val response = booksRepository.getBooks()
            Log.d("TAGGGGG", "fetchBooks: ${response.data}")
            if (response.data != null)
                _books.value = response.data.data.book
        }

    }

    fun fetchAudiItemsByBookId(id: Int) {
        Log.d("FFFFUUCCK", "fetchAudiItemsByBookId: some shit should happen$id ")
        viewModelScope.launch {
            val response = booksRepository.getBook(id)
            when (response) {
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    _currentMediaItems.postValue(response.data!!.data.audio)
                }
            }
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
                        Audio(
                            _id = it.mediaId!!,
                            name = it.description.title.toString(),
                            file = it.description.mediaUri.toString(),
                            number = children.indexOf(it) + 1,
                            duration = 0.0,
                            size = 0
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

    fun playOrToggle(mediaItem: Audio, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
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