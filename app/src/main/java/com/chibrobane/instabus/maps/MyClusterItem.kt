package com.chibrobane.instabus.maps

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.chibrobane.instabus.BusStopDetailsActivity
import com.chibrobane.instabus.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


public class MyClusterItem (
        lat: Double,
        lng: Double,
        title: String,
        snippet: String
    ) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }

    init {
        position = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
    }
}