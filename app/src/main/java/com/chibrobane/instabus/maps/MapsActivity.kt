package com.chibrobane.instabus.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.chibrobane.instabus.BusStopDetailsActivity
import com.chibrobane.instabus.MainActivity
import com.chibrobane.instabus.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var clusterItem: ClusterManager<ClusterItem>
    //importation de la liste des bus
    private var busStops = MainActivity.busStops

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        setSupportActionBar(findViewById(R.id.mytoolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Carte"

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpCluster()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.secondary_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun showBuses(title: String){
        val intent = Intent(this, BusStopDetailsActivity::class.java).apply {
            putExtra("name_bus_stop", title)
        }
        startActivity(intent)
    }

    private fun setUpCluster(){
        clusterItem = ClusterManager(this, mMap)

        val firstCoordinate = LatLng(41.39, 2.15)//coordonnées de Barça (pour qu'à l'arrivée on se retrouve au dessus de la ville)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstCoordinate, 9.0f))
        mMap.setOnCameraIdleListener(clusterItem)
        addItems()
    }

    private fun addItems(){
        //on itère sur toute la liste pour récup les coordonnées de chaque arrêt et on les affiche sur la map
        for (busTop in busStops){
            val offsetItem = MyClusterItem(busTop.lat, busTop.lon, busTop.street_name, busTop.id.toString())
            clusterItem.addItem(offsetItem)
        }
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        showBuses(marker.title)
        return true
    }
}