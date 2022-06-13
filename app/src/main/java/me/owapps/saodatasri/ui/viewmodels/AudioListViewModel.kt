package me.owapps.saodatasri.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Requirements
import dagger.hilt.android.lifecycle.HiltViewModel
import me.owapps.saodatasri.data.entities.Raw
import me.owapps.saodatasri.data.remote.MediaSource
import me.owapps.saodatasri.exoplayer.download.MyDownloadService
import javax.inject.Inject

@HiltViewModel
class AudioListViewModel @Inject constructor(private val mediaSource: MediaSource) : ViewModel() {

    fun download(raw: Raw, context: Context) {


        DownloadService.sendAddDownload(
            context,
            MyDownloadService::class.java,
            getDownloadRequest(raw),
            false
        )
    }

    private fun getDownloadRequest(raw: Raw): DownloadRequest =
        DownloadRequest.Builder(raw._id, Uri.parse(raw.link))
            .build()
}