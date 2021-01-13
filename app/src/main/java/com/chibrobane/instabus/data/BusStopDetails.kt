package com.chibrobane.instabus.data

import android.media.Image
import android.net.Uri

data class BusStopDetails (
        val title: String,
        val image: Uri?,
        val date_of_creation: String
)