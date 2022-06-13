package me.owapps.saodatasri.util

import me.owapps.saodatasri.data.entities.Raw

interface AudioItem {
    fun onPlay(raw: Raw)
    fun onDownload(raw: Raw)
}