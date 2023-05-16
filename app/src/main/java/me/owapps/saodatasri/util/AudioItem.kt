package me.owapps.saodatasri.util

import me.owapps.saodatasri.data.entities.book.Audio

interface AudioItem {
    fun onPlay(raw: Audio)
    fun onDownload(raw: Audio)
}