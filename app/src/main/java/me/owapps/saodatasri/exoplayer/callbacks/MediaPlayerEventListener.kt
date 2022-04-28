package me.owapps.saodatasri.exoplayer.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import me.owapps.saodatasri.exoplayer.PlayerService

class MediaPlayerEventListener(private val playerService: PlayerService) : Player.Listener {
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_READY && !playWhenReady) {
            playerService.stopForeground(false)
        }
    }
//
//    override fun onPlaybackStateChanged(playbackState: Int) {
//        super.onPlaybackStateChanged(playbackState)
//        if (playbackState == Player.STATE_READY){
//            musicService.stopForeground(false)
//        }
//    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(playerService, "An unknown error occured", Toast.LENGTH_LONG).show()
    }

}