package me.owapps.saodatasri.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import dagger.hilt.android.lifecycle.HiltViewModel
import me.owapps.saodatasri.data.entities.book.Audio
import me.owapps.saodatasri.data.remote.MediaSource
import me.owapps.saodatasri.exoplayer.download.MyDownloadService
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(private val mediaSource: MediaSource) : ViewModel() {

    fun download(raw: Audio, context: Context) {


        DownloadService.sendAddDownload(
            context,
            MyDownloadService::class.java,
            getDownloadRequest(raw),
            false
        )
    }

    private fun getDownloadRequest(raw: Audio): DownloadRequest =
        DownloadRequest.Builder(raw._id, Uri.parse(raw.file))
            .build()
}